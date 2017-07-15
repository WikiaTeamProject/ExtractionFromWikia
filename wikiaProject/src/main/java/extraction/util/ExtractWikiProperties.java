package extraction.util;

import extraction.model.WikiaWikiProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class allows to extract various properties of wikis.
 */
public class ExtractWikiProperties {

    private static Logger logger = Logger.getLogger(LanguageCodes.class.getName());

    public WikiaWikiProperties extractPropertiesForaWiki(String wikiFilePath) {

        WikiaWikiProperties wikiProperties=null;
        // get the path to the dbpedia extraction framework


        try {
            String line = "";
            String fileContents = "";
            String languageCode = "";
            String wikiName="";
            String wikiPath=wikiFilePath;
            int lineNumber = 0;


            FileReader fr = new FileReader(wikiFilePath);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null && lineNumber <= 20) {

                fileContents += line;
                lineNumber++;
            }

            if(fileContents.length()>0) {
                System.out.println(wikiFilePath);

                languageCode = fileContents.substring(fileContents.indexOf("xml:lang=") + 10, fileContents.indexOf(">", fileContents.indexOf("xml:lang=") + 10) - 1);

                wikiName = (fileContents.substring(fileContents.indexOf("<sitename>") + 10, fileContents.indexOf("</sitename>", fileContents.indexOf("<sitename>") + 10) - 1)).trim().replace(" ", "_");

                // wikiProperties = new WikiaWikiProperties(wikiName, languageCode, wikiPath);

            }
            br.close();
            fr.close();


        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;
    }

    /**
     *
     *
     */
    public HashMap<String,WikiaWikiProperties> extractPropertiesForAllWikis() {

        //WikiaWikisProperties
        HashMap<String,WikiaWikiProperties> wikiProperties=new HashMap<String,WikiaWikiProperties>();


        String wikisFilePath= ResourceBundle.getBundle("config").getString("dumpsdirectory");

        try {
            File wikisFilesFolder = new File(wikisFilePath);

            //get list of wikis in a folder
            File[] listOfFolders = wikisFilesFolder.listFiles();

            for(File formatFolder:listOfFolders){

                if(formatFolder.isDirectory()) {

                    File[] listOfFiles=formatFolder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {

                        if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".xml")) {

                            WikiaWikiProperties properties = extractPropertiesForaWiki(listOfFiles[i].getPath());

                            if (wikiProperties != null)
                                wikiProperties.put(listOfFiles[i].getName(), properties);
                        }
                    }
                }

            }
        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;
    }
}
