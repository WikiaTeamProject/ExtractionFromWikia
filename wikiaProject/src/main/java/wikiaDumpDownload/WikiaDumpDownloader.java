package wikiaDumpDownload;

import java.io.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by Jan Portisch on 25.03.2017.
 */
public class WikiaDumpDownloader implements Runnable {

    public static void main(String[] args) {
        Logger mainlogger = Logger.getLogger(WikiaDumpDownloader.class.getName());
        String directoryPath = ResourceBundle.getBundle("config").getString("directory");
        String filePath = directoryPath + "/wikiaOverviewIndividualFiles/p1_wikis_1_to_500000.csv";
        File f = new File(filePath);

        Thread t1 = new Thread(new WikiaDumpDownloader(f, 1, 10));
        t1.run();

    }

    // variables with getters and setters
    private boolean downloadAllWikis; // indicates whether all wikis shall be downloaded
    private int beginAtLine; // start processing the document at the specified line
    private int endAtLine;  // end processing the document at the specified line
    private File fileToReadFrom; // file from which will be read

    // internal variables
    private Logger logger = Logger.getLogger(WikiaDumpDownloader.class.getName());
    private LineNumberReader lineReader;


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
            lineReader = new LineNumberReader(new FileReader(fileToReadFrom));

            String readLine;
            String[] tokens;

            // jump to line
            lineReader.readLine();
            while (lineReader.getLineNumber() < beginAtLine - 1) {
                lineReader.readLine();
            }

            processFile:
            while ((readLine = lineReader.readLine()) != null) {
                tokens = readLine.split(";");
                System.out.println(tokens[1]);

                // TODO: !!! Continue here !!!



                // check whether the specified end line was reached (endAtLine) -> yes: leave loop; no: continue
                if (!this.downloadAllWikis) {
                    if (lineReader.getLineNumber() == endAtLine) {
                        break processFile;
                    }
                }

            }

        } catch (FileNotFoundException fnfe) {
            logger.severe(fnfe.toString());
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

    }
}
