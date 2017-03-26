package wikiaDumpDownload.controller;

import java.io.*;
import java.util.ResourceBundle;

/**
 * This class represents the implementation of WikiaDumpDownloadThread threads.
 */
public class WikiaDumpDownloadThreadImpl  {

    public static void downloadWikiaDumps() {
        String directoryPath = ResourceBundle.getBundle("config").getString("directory");
        String filePath = directoryPath + "/wikiaAllOverview.csv";
        File f = new File(filePath);

        Thread t1 = new Thread(new WikiaDumpDownloadThread(f, 0, 10));
        t1.run();

    }

}
