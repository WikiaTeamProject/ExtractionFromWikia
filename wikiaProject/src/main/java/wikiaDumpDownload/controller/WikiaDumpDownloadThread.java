package wikiaDumpDownload.controller;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class reads the wikiaAllOverview file with metadata of all wikis and downloads a dump of each wiki.
 * This class is optimized for multi-threading.
 */
public class WikiaDumpDownloadThread implements Runnable {

    // variables with getters and setters
    private boolean downloadAllWikis; // indicates whether all wikis shall be downloaded
    private int beginAtLine; // start processing the document at the specified line
    private int endAtLine;  // end processing the document at the specified line
    private File fileToReadFrom; // file from which will be read

    // internal variables
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private LineNumberReader fileReader;
    private BufferedReader urlReader;
    private URL url;
    private URLConnection urlConnection;
    private String directoryPath = ResourceBundle.getBundle("config").getString("directory");
    private int downloaded = 0;
    private int wikis = 0;
    private StringBuffer buffer = new StringBuffer();


    /**
     * Constructor
     *
     * @param fileToReadFrom read the URLs from the specified file
     * @param beginAtLine    start processing the document at the specified line; cannot be less than 2
     * @param endAtLine      end processing the document at the specified line (including the specified line); cannot be less than 3
     */
    public WikiaDumpDownloadThread(File fileToReadFrom, int beginAtLine, int endAtLine) {
        this.downloadAllWikis = false;
        this.fileToReadFrom = fileToReadFrom;

        // make sure beginning line is at least 2 (because of header line)
        this.beginAtLine = Math.max(beginAtLine, 2);
        this.endAtLine = Math.max(endAtLine, 3);
    }


    /**
     * Constructor
     *
     * @param fileToReadFrom read the URLs from the specified file
     */
    public WikiaDumpDownloadThread(File fileToReadFrom) {
        this.downloadAllWikis = true;
        this.fileToReadFrom = fileToReadFrom;
    }


    /**
     * Run method for the thread.
     */
    public void run() {

        try {

            fileReader = new LineNumberReader(new FileReader(fileToReadFrom));

            String readLineFromFile;
            String readLineFromURL;
            String regExToFindURL; // regex used to filter the download URL
            String[] tokens; // contains the tokens from the CSV file
            String queryLink; // contains the link where the downloadlink can be found e.g. "http://babylon5.wikia.com/wiki/Special:Statistics"
            String pathToFileToDownload = ""; // contains the path where the XML can be downloaded e.g. "http://s3.amazonaws.com/wikia_xml_dumps/b/ba/babylon5_pages_current.xml.7z"
            Pattern pattern; // for regex search
            Matcher matcher; // for regex search
            StringBuffer urlsNotWorking = new StringBuffer("URLs not working:" + "\n");

            // jump to line
            fileReader.readLine();
            while (fileReader.getLineNumber() < beginAtLine - 1) {
                fileReader.readLine();
            }

            processFile:
            while ((readLineFromFile = fileReader.readLine()) != null) {

                // check whether the specified end line was reached (endAtLine) -> yes: leave loop; no: continue
                if (!this.downloadAllWikis) {
                    if (fileReader.getLineNumber() >= endAtLine) {
                        break processFile;
                    }
                }
                wikis++;

                tokens = readLineFromFile.split(";");

                queryLink = tokens[1] + "wiki/Special:Statistics";
                logger.info("Processing: " + queryLink);


                url = new URL(queryLink);

                try {
                    urlConnection = url.openConnection();

                    // read from URL object
                    urlReader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));

                } catch(IOException ioe) {
                    urlsNotWorking.append(queryLink + "\n");
                }


                while ((readLineFromURL = urlReader.readLine()) != null) {

                    if (readLineFromURL.contains("wikia_xml_dumps")) {

                        regExToFindURL = "http:.*7z"; // correct regex (unmasked): http:.*7z
                        pattern = Pattern.compile(regExToFindURL);
                        matcher = pattern.matcher(readLineFromURL);
                        if (matcher.find()) {
                            pathToFileToDownload = matcher.group(0);

                            saveRemoteFile(pathToFileToDownload);
                        } else {
                            regExToFindURL = "http:.*gz";
                            pattern = Pattern.compile(regExToFindURL);
                            matcher = pattern.matcher(readLineFromURL);
                            if (matcher.find()) {
                                pathToFileToDownload = matcher.group(0);
                                saveRemoteFile(pathToFileToDownload);
                            }
                        }

                        // a download link was found -> continue with the next URL from the file
                        continue processFile;

                    }

                } // end of URL line reader loop

                buffer.append(queryLink + ";-" + "\n");
                logger.info("No wikia dump exists for wiki: " + queryLink);

            } // end of file writer loop
            saveSizeToFile();
            logger.info("Download finished. Downloaded " + downloaded + " of " + wikis + " wikis.");
            logger.info(urlsNotWorking.toString());

            // closing readers
            fileReader.close();
            urlReader.close();

        } catch (FileNotFoundException fnfe) {
            logger.severe(fnfe.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

    } // end of run method


    /**
     * This method will save the file to the directory wikiaDumps of the specified directory in config.properties
     * @param pathToRemoteFile the URL to the file
     */
    private void saveRemoteFile (String pathToRemoteFile) {

        URL url;
        FileOutputStream fos;
        ReadableByteChannel rbc;

        // use regex to get the correct file name
        String fileName = "";
        String regEx = "(?<=\\/)[^\\/]*$"; // correct regex (unmasked): (?<=\/)[^\/]*$
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pathToRemoteFile);
        if (matcher.find()) {
            fileName = matcher.group(0);
        }

        // create target file and create directory if it does not exist yet
        File file = new File(directoryPath + "/wikiaDumps/");
        if (!file.isDirectory()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }

        File targetFile = new File(directoryPath + "/wikiaDumps/" + fileName);
        logger.info("Writing file " + fileName);

        try {
            url = new URL(pathToRemoteFile);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            rbc = Channels.newChannel(inputStream);
            fos = new FileOutputStream(targetFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            int size = connection.getContentLength();
            logger.info("Size of file: " + (size / 1024) + " KB.");
            buffer.append(fileName + ";" + (size/1024) + "\n");
            downloaded++;

            // closing streams
            connection.getInputStream().close();
            try {
                fos.close();
            } catch(IOException ioe) {
                logger.severe(ioe.toString());
            }
            rbc.close();

        } catch (MalformedURLException mue) {
            logger.severe(mue.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }


    }

    private void saveSizeToFile() {
        File file = new File(directoryPath + "/wikiaDumpsSizes.csv");
        logger.info("Writing file " + file.getName());

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("Wiki URL/file;KB" + "\n");
            bufferedWriter.write(buffer.toString());
            bufferedWriter.close();

        } catch (IOException e) {

            logger.severe(e.toString());
        }


    }

}
