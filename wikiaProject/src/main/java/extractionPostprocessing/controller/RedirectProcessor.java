package extractionPostprocessing.controller;

import extraction.Extractor;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class processes the redirect file.
 */
public class RedirectProcessor {

    private HashMap<String, String> redirectsMap = new HashMap<>();
    private static Logger logger = Logger.getLogger(Extractor.class.getName());
    private String filePathToWiki;


    public static void main(String[] args) {
        String filePath = "C:\\Users\\D060249\\Desktop\\TMP\\Wikia_Root\\Pokemon_Go";
        RedirectProcessor rp = new RedirectProcessor(filePath);
        rp.readRedirects();

    }


    /**
     * Constructor
     * @param filePathToWiki File path to the wiki files.
     */
    public RedirectProcessor(String filePathToWiki){
        setFilePathToWiki(filePathToWiki);
    }

    /**
     * Read the redirect file.
     * @return Returns true if reading was successful, else false.
     */
    public boolean readRedirects(){

        File wikiDirectory = new File(filePathToWiki);

        // make sure the directory actually is a directory
        if(!wikiDirectory.isDirectory()){
            logger.severe("Given filePathToWiki does not lead to a directory.");
            return false;
        }

        // get the redirect file
        File redirectFile = null;
        for(File f : wikiDirectory.listFiles()){
            System.out.println(f.getName());
            if(f.getName().endsWith("-redirects.ttl")) {
                // -> redirects file found
                redirectFile = f;
                break;
            }
        }

        System.out.println(redirectFile.getName());

        try {
            BufferedReader br = new BufferedReader(new FileReader(redirectFile));

            String line;
            Matcher matcher = null;
            while((line = br.readLine()) != null)
            {
                System.out.println(line);
                Pattern pattern = Pattern.compile("<[^<]*>");
                // regex: <[^<]*>
                // this regex captures everything between tags including the tags: <...>
                // there are three tags in every line

                matcher = pattern.matcher(line);

                int index = 0;
                String key = null;
                String value = null;

                while(matcher.find()){
                    index++;
                    if(index == 1){
                        // first match: key
                        key = matcher.group();
                        System.out.println(matcher.group());
                    }

                    // (second match: "<http://dbpedia.org/ontology/wikiPageRedirects>"
                    // always the same -> irrelevant for us

                    if(index == 3){
                        value = matcher.group();
                        System.out.println(matcher.group());

                        if(key != null && value != null){
                            // key and value found -> add to map and break while loop
                            redirectsMap.put(key, value);
                            key = null;
                            value = null;
                            break;
                        }
                    }
                }
            }
            br.close();

        } catch (IOException ioe) {
            logger.severe(ioe.toString());
            return false;
        }
        return true;
    }





    //
    // IN THE FOLLOWING YOU WILL ONLY FIND GETTERS AND SETTERS
    //

    public String getFilePathToWiki() {
        return filePathToWiki;
    }

    public void setFilePathToWiki(String filePathToWiki) {
        this.filePathToWiki = filePathToWiki;
    }
}
