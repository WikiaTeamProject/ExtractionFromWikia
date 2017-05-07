package extraction.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LanguageCodes {

    private static Logger logger = Logger.getLogger(LanguageCodes.class.getName());

    /**
     * This method receives a folder which includes xml files and returns their language codes (xml:lang attribute).
     *
     * @param wikisFilePath
     */

    public static void extractLanguageCodeForAllWikis(String wikisFilePath) {

        try {
            File wikisFilesFolder = new File(wikisFilePath);

            //get list of wikis in a folder
            File[] listOfFiles = wikisFilesFolder.listFiles();


            //result variable
            String wikisLanguageCode = "";

            //intialize header for CSV file
            wikisLanguageCode += "Wikia_Name,Language_Codes" + "\n";


            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile()) {

                    String line = "";
                    String fileContents = "";
                    String languageCode = "";
                    int lineNumber = 0;


                    FileReader fr = new FileReader(listOfFiles[i].getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);

                    while ((line = br.readLine()) != null && lineNumber <= 10) {

                        fileContents += line;
                        lineNumber++;

                    }


                    languageCode = fileContents.substring(fileContents.indexOf("xml:lang=") + 10, fileContents.indexOf(">", fileContents.indexOf("xml:lang=") + 10) - 1);

                    wikisLanguageCode += listOfFiles[i].getName() + "," + languageCode + "\n";

                    br.close();
                    fr.close();

                }
            }

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }
}
