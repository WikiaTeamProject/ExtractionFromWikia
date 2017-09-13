package applications.wikiaDumpDownload.controller;

import org.apache.commons.lang3.StringUtils;
import utils.IOoperations;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class reads the wikiaAllOverview file with metadata of all wikis and downloads a dump of each wiki.
 * This class is optimized for multi-threading.
 */
public class WikiaDumpDownloadThread implements Runnable {

    private static final Logger logger = Logger.getLogger(WikiaDumpDownloadThread.class.getName());

    // variables with getters and setters
    private boolean downloadAllWikis; // indicates whether all wikis shall be downloadedFiles
    private int beginAtLine; // start processing the document at the specified line
    private int endAtLine;  // end processing the document at the specified line

    // internal variables
    private String directoryPath;
    private int downloadedFiles;
    private int wikis;
    private StringBuffer buffer;
    private StringBuffer urlsNotWorking;
    private File dumpsDownloadedgz;
    private File dumpsDownloaded7z;
    private String dumpSizeFilePath;
    private static int totalNumberOfFilesProcessed = 0; // class counter to output progress
    private static int totalNumberOfFilesToBeProcessed = 0; // class variable
    private static final String[] REGEX = {"http:.*current\\.xml\\.gz", "http:.*current\\.xml\\.7z"}; // unmasked regex "http:.*current\.xml\.7z"
    private HashMap<String,String> dumpFilesURL;
    private String dumpURLsFilePath;
    private HashMap<String, String> languageCodes;

    private ArrayList<String> urls;

    /**
     *  Main Constructor - main constructor for general initialization (also without file) -> private
     */
    private WikiaDumpDownloadThread() {

        this.directoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

        this.downloadedFiles = 0;
        this.wikis = 0;
        this.buffer = new StringBuffer();

        createTargetDirectories();

        this.urlsNotWorking = new StringBuffer();

        readWikiaLanguageCodes();

        // files will be saved in the newly created subdirectory
        String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/statistics";
        File file = new File(statisticsDirectoryPath);
        if (! file.exists())
            IOoperations.createDirectory(statisticsDirectoryPath);

    }

    /**
     *   Constructor - constructor for initialization without file
     */
    public WikiaDumpDownloadThread(List<String> urls, String dumpSizeFilePath, String dumpURLsFilePath) {

        this();

        this.wikis = urls.size();
        this.totalNumberOfFilesToBeProcessed = urls.size();
        this.urls = new ArrayList<>(urls);
        this.dumpSizeFilePath = dumpSizeFilePath;
        this.dumpURLsFilePath = dumpURLsFilePath;

    }

    /**
     *  Constructor - main constructor for initialization with file -> private
     * @param downloadAllWikis
     * @param pathToReadFrom
     */
    private WikiaDumpDownloadThread(boolean downloadAllWikis, String pathToReadFrom) {

        this();

        // initialize static counter to display progress
        if (totalNumberOfFilesToBeProcessed == 0) {
            String filePath = this.directoryPath + "/statistics/wikiaAllOverview.csv";
            File f = new File(filePath);

            // make sure that the file exists
            if (f.exists()) {
                try {
                    LineNumberReader lnr = new LineNumberReader(new FileReader(f));
                    lnr.skip(Long.MAX_VALUE);

                    // set the total number of lines
                    totalNumberOfFilesToBeProcessed = lnr.getLineNumber(); //Add 1 because line index starts at 0
                    lnr.close();
                } catch (IOException ioe){
                    logger.severe(ioe.toString());
                }
            }
        }

        this.downloadAllWikis = downloadAllWikis;

        this.urls = getUrls(pathToReadFrom);
    }


    /**
     * Constructor
     *
     * @param pathToReadFrom read the URLs from the specified file
     * @param beginAtLine    start processing the document at the specified line; cannot be less than 2
     * @param endAtLine      end processing the document at the specified line (including the specified line); cannot be less than 3
     */
    public WikiaDumpDownloadThread(String pathToReadFrom, int beginAtLine, int endAtLine) {
        this(false, pathToReadFrom);

        // make sure beginning line is at least 1 (because of header line - 0)
        this.beginAtLine = Math.max(beginAtLine, 1);
        this.endAtLine = Math.max(endAtLine, 1);
    }


    /**
     * Constructor
     *
     * @param pathToReadFrom read the URLs from the specified file
     */
    public WikiaDumpDownloadThread(String pathToReadFrom, String dumpSizeFilePath, String dumpURLsFilePath) {
        this(true, pathToReadFrom);
        // beginning line is 1 (because of header line - 0)
        this.beginAtLine = 1;
        this.dumpSizeFilePath = dumpSizeFilePath;
        this.dumpURLsFilePath = dumpURLsFilePath;
    }



    /**
     * Run method for the thread.
     */
    public void run() {


        urls.forEach(url -> {
            if (shouldLanguageBeDownloaded(url))
                downloadDump(url);
        });

        logger.info("Download finished. Downloaded " + downloadedFiles + " of " + wikis + " wikis.");

        if (dumpSizeFilePath != null ) {
            saveSizeToFile();
            saveDumpURLsToFile();
        }

        if (urlsNotWorking.length() == 0) {
            logger.info("No URLs that did not work after retries");
        } else {
            logger.info("URLs that did not work for downloading after retries:\n" + urlsNotWorking.toString());
        }
    }


    /**
     * Retrieve URLs from csv files with wikis
     *
     * @param filePath
     * @return an array list with all urls to the statistics pages of all wikia wikis
     */
    public ArrayList<String> getUrls(String filePath) {
        LineNumberReader fileReader;
        String line;
        String[] tokens;
        ArrayList<String> urls = new ArrayList<String>();

        // create file with specified file path
        File file = new File(filePath);

        // read lines of file
        try {
            fileReader = new LineNumberReader(new FileReader(file));

            // ignore header line
            fileReader.readLine();
            while (fileReader.getLineNumber() < beginAtLine) {
                fileReader.readLine();
            }

            while ((line = fileReader.readLine()) != null) {

                // check whether the specified end line was reached (endAtLine) -> yes: leave loop; no: continue
                if (!this.downloadAllWikis) {
                    if (fileReader.getLineNumber() > endAtLine + 1) {
                        break;
                    }
                }
                wikis++;

                // split line into tokens
                tokens = line.split(";");

                // add url to urls list
                urls.add(tokens[1]);
            }

            // close stream
            fileReader.close();

        } catch (FileNotFoundException fnfe) {
            logger.severe(fnfe.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

        return urls;
    }

    private void createTargetDirectories() {
        // create target directories if they do not exist yet
        File dumps = IOoperations.createDirectory(directoryPath + "/downloadedWikis/");
        File dumpsDownloaded = IOoperations.createDirectory(dumps.getPath() + "/downloaded");
        dumpsDownloadedgz = IOoperations.createDirectory(dumpsDownloaded.getPath() + "/gz");
        dumpsDownloaded7z = IOoperations.createDirectory(dumpsDownloaded.getPath() + "/7z");
    }

    /**
     * read wikiaLanguageCodes file into hashmap
     */
    private void readWikiaLanguageCodes() {
        this.languageCodes = new HashMap<>();
        String path = WikiaDumpDownloadThread.class.getClassLoader().getResource("files/wikiaLanguageCodes.csv").getPath();
        File file = new File(path);

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader( new FileReader(file));
                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    String[] line = sCurrentLine.split(";");
                    languageCodes.put(line[0], line[1]);
                }
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }
    }

    /**
     * Check from url whether it is an English wiki
     * @param url
     * @return
     */
    private boolean shouldLanguageBeDownloaded(String url) {
        boolean languageWanted = false;
        String prefix = StringUtils.substringBetween(url,"http://", ".");
        String[] languageCodesToDownload = ResourceBundle.getBundle("config").getString("languages").split(",");
        ArrayList<String> languages = new ArrayList<>(Arrays.asList(languageCodesToDownload));

        // english wiki
        if (! languageCodes.containsKey(prefix) && languages.contains("en")) {
            languageWanted = true;
        // other language than English wanted, specified in config.properties
        } else if (languageCodes.containsKey(prefix) && languages.contains(prefix)) {
            languageWanted = true;
        }

        return languageWanted;
    }

    /**
     * Check if dump exists for wiki and save the file to the specified directory
     *
     * @param baseURL
     */
    private void downloadDump(String baseURL) {
        boolean foundDump = false;
        URL query;
        URLConnection connection;
        BufferedReader urlReader;
        String readLineFromURL;
        String pathToFileToDownload;

        String url = baseURL + "/wiki/Special:Statistics";

        logger.info("Processing: " + url);

        try {

            query = new URL(url);
            connection = query.openConnection();
            urlReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((readLineFromURL = urlReader.readLine()) != null && !foundDump) {

                if (readLineFromURL.contains("wikia_xml_dumps")) {

                    pathToFileToDownload = findRegexInLine(REGEX, readLineFromURL);
                    if (!pathToFileToDownload.isEmpty()) {
                        getConnectionToRemoteFileAndSave(pathToFileToDownload,baseURL);
                    }

                    foundDump = true;
                }
            }

            if (!foundDump) {
                logger.info("No wikia dump exists for wiki: " + url);
                buffer.append(url + ";-" + "\n");
            }

            System.out.println(++totalNumberOfFilesProcessed + " out of " + totalNumberOfFilesToBeProcessed + " processed.");

            // close stream
            urlReader.close();


        } catch (MalformedURLException murle) {
            logger.severe(murle.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }
    }


    /**
     * Check if one of the REGEX constants matches the specified line
     *
     * @param regEx
     * @param line
     * @return
     */
    private String findRegexInLine(String[] regEx, String line) {
        Pattern pattern;
        Matcher matcher;
        String pathToFileToDownload = ""; // contains the path where the XML can be downloadedFiles e.g. "http://s3.amazonaws.com/wikia_xml_dumps/b/ba/babylon5_pages_current.xml.7z"

        for (String x : regEx) {
            pattern = Pattern.compile(x);
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                pathToFileToDownload = matcher.group(0);
                return pathToFileToDownload;
            }
        }

        return pathToFileToDownload;
    }

    /**
     * This method will save the file to the directory wikiaDumps of the specified directory in config.properties
     * @param pathToRemoteFile the URL to the file
     */
    private void getConnectionToRemoteFileAndSave(String pathToRemoteFile, String baseURL) {
        int retry = 0;
        boolean downloaded = false;
        URL url;


        // retry download up to 5 times
        while (retry < 5 && !downloaded) {

            try {
                retry++;

                url = new URL(pathToRemoteFile);
                URLConnection connection = url.openConnection();
                int size = connection.getContentLength();
                logger.info("Size of file: " + (size / 1024) + " KB.");
                buffer.append(pathToRemoteFile + ";" + (size / 1024) + "\n");
                downloadedFiles++;

                // comment in if you want to actually download the files
                saveFile(connection, pathToRemoteFile, baseURL);

                // closing streams
                connection.getInputStream().close();

                // successfully downloadedFiles - do not retry
                downloaded = true;

            } catch (MalformedURLException mue) {
                logger.severe(mue.toString());
            } catch (IOException ioe) {
                logger.severe(ioe.toString());
            }
        }

        if (! downloaded)
            urlsNotWorking.append(pathToRemoteFile);

    }

    /**
     * Save dump file to specified directory
     *
     * @param connection
     * @param pathToRemoteFile
     */
    private void saveFile(URLConnection connection, String pathToRemoteFile, String baseURL) {
        FileOutputStream fos;
        ReadableByteChannel rbc;
        File targetFile;
        InputStream inputStream = null;

        // use regex to get the correct file name (without / and so on)
        String fileName = "";
        String regEx = "(?<=\\/)[^\\/]*$"; // correct regex (unmasked): (?<=\/)[^\/]*$
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pathToRemoteFile);
        if (matcher.find()) {
            fileName = matcher.group(0);
        }

        if (fileName.endsWith("7z")) {
            targetFile = new File(dumpsDownloaded7z.getPath() + "/" + fileName);
        } else {
            targetFile = new File(dumpsDownloadedgz.getPath() + "/" + fileName);
        }
        logger.info("Writing file " + fileName);


        try {
            inputStream = connection.getInputStream();
            rbc = Channels.newChannel(inputStream);
            fos = new FileOutputStream(targetFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            if (dumpFilesURL == null)
                dumpFilesURL = new HashMap<String,String>();

            dumpFilesURL.put(targetFile.getName(),baseURL);

           System.out.println("*******" + targetFile.getAbsolutePath() + " : " + baseURL);

            fos.close();
            rbc.close();

        } catch (FileNotFoundException fnfe) {
            logger.severe(fnfe.toString());
        } catch (IOException e) {
            logger.severe(e.toString());
        }

    }

    /**
     * Save size of dump file (saved in buffer) to csv, also includes urls which do not have a dump
     */
    private void saveSizeToFile() {
        File file = new File(this.dumpSizeFilePath);
        logger.info("Writing file: " + file.getName());

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("Wiki URL/file;KB" + "\n");
            bufferedWriter.write(buffer.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            logger.severe(e.toString());
        }

    }


    /**
     * This function saves dumps base URLs into file
     */
    private void saveDumpURLsToFile() {
        File dumpURLsFolder = new File(this.dumpURLsFilePath.substring(0, dumpURLsFilePath.lastIndexOf("/")));
        File file = new File(this.dumpURLsFilePath);
        try {

            if (! dumpURLsFolder.exists())
                dumpURLsFolder.mkdir();

            logger.info("Writing file: " + file.getName());

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("dump_file_path,base_url" + "\n");

            for (String dumpPath : dumpFilesURL.keySet()) {
                bufferedWriter.write(dumpPath+"," + dumpFilesURL.get(dumpPath) + "\n");
            }

            bufferedWriter.close();

        } catch (IOException e) {
            logger.severe(e.toString());
        }

    }
}
