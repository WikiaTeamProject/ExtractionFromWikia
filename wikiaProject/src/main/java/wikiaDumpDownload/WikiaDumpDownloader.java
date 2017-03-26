package wikiaDumpDownload;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Jan Portisch on 25.03.2017.
 */
public class WikiaDumpDownloader implements Runnable {

    public static void main(String[] args) {
        Logger mainlogger = Logger.getLogger(WikiaDumpDownloader.class.getName());
        String directoryPath = ResourceBundle.getBundle("config").getString("directory");
        String filePath = directoryPath + "/wikiaOverviewIndividualFiles/p1_wikis_1_to_500000.csv";
        File f = new File(filePath);

        Thread t1 = new Thread(new WikiaDumpDownloader(f, 1, 5));
        t1.run();

    }

    // variables with getters and setters
    private boolean downloadAllWikis; // indicates whether all wikis shall be downloaded
    private int beginAtLine; // start processing the document at the specified line
    private int endAtLine;  // end processing the document at the specified line
    private File fileToReadFrom; // file from which will be read

    // internal variables
    private Logger logger = Logger.getLogger(WikiaDumpDownloader.class.getName());
    private LineNumberReader fileReader;
    private BufferedReader urlReader;
    private URL url;
    private URLConnection urlConnection;
    private String directoryPath = ResourceBundle.getBundle("config").getString("directory");


    /**
     * Constructor
     *
     * @param fileToReadFrom read the URLs from the specified file
     * @param beginAtLine    start processing the document at the specified line; cannot be less than 2
     * @param endAtLine      end processing the document at the specified line (including the specified line); cannot be less than 3
     */
    public WikiaDumpDownloader(File fileToReadFrom, int beginAtLine, int endAtLine) {
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
    public WikiaDumpDownloader(File fileToReadFrom) {
        this.downloadAllWikis = true;
        this.fileToReadFrom = fileToReadFrom;
    }


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


                tokens = readLineFromFile.split(";");

                queryLink = tokens[1] + "wiki/Special:Statistics";
                logger.info("Processing: " + queryLink);

                url = new URL(queryLink);
                urlConnection = url.openConnection();

                // read from URL object
                urlReader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));

                while ((readLineFromURL = urlReader.readLine()) != null) {

                    if (readLineFromURL.contains("wikia_xml_dumps")) {

                        regExToFindURL = "http:.*7z"; // correct regex (unmasked): http:.*7z
                        pattern = Pattern.compile(regExToFindURL);
                        matcher = pattern.matcher(readLineFromURL);
                        if (matcher.find()) {
                            pathToFileToDownload = matcher.group(0);

                            // TODO: Write 7zip extractor...

                            // saveGzipRemoteFile(pathToFileToDownload);
                        } else {
                            regExToFindURL = "http:.*gz";
                            pattern = Pattern.compile(regExToFindURL);
                            matcher = pattern.matcher(readLineFromURL);
                            if (matcher.find()) {
                                pathToFileToDownload = matcher.group(0);
                                saveGzipRemoteFile(pathToFileToDownload);
                            }
                        }

                        // a download link was found -> continue with the next URL from the file
                        continue processFile;

                    }


                } // end of URL line reader loop

            } // end of file writer loop


            logger.info("Download was finished.");


            // closing readers
            try {
                fileReader.close();
            } catch (IOException ioe) {
                logger.severe(ioe.toString());
                urlReader.close();
            }
            urlReader.close();

        } catch (FileNotFoundException fnfe) {
            logger.severe(fnfe.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

    } // end of run method



    /**
     * This method will save the file which is specified through the parameter in /resources/files/wikiaDumps
     *
     * @param pathToRemoteFile the URL to the file
     */
    private void saveGzipRemoteFile(String pathToRemoteFile) {
        BufferedReader remoteReader;
        BufferedWriter fileWriter;
        URL url;
        URLConnection urlConnection;
        String readLine;


        // use regex to get the correct file name
        String fileName = "";
        String regEx = "(?<=\\/)[^\\/]*$"; // correct regex (unmasked): (?<=\/)[^\/]*$
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pathToRemoteFile);
        if (matcher.find()) {
            fileName = matcher.group(0);
        }

        // create target file
        File targetFile = new File(directoryPath + "/wikiaDumps/" + fileName);


        /**
         *
         *  GZIPOutputStream zip = new GZIPOutputStream(
         new FileOutputStream(new File("tmp.zip")));

         writer = new BufferedWriter(
         new OutputStreamWriter(zip, "UTF-8"));
         *
         */


        try {
            url = new URL(pathToRemoteFile);
            urlConnection = url.openConnection();

            // set up compressed reader
            GZIPInputStream gzipInputStream = new GZIPInputStream(urlConnection.getInputStream());
            remoteReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));

            // set up compressed writer
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(targetFile));
            fileWriter = new BufferedWriter(new OutputStreamWriter(gzipOutputStream, "UTF-8"));
            logger.info("Writing file " + fileName);

            // write process
            while((readLine = remoteReader.readLine()) != null) {
                fileWriter.write(readLine + "\n");
            }

            fileWriter.flush();

            // closing reader and writer
            try {
                remoteReader.close();
            } catch (IOException ioe) {
                fileWriter.close();
            }
            fileWriter.close();

        } catch (MalformedURLException mue){
            logger.severe(mue.toString());
        } catch(IOException ioe) {
            logger.severe(ioe.toString());
        }

    }


}
