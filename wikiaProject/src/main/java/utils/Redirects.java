package utils;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 * A class for storing redirects.
 */
public class Redirects {

    private static Redirects redirectsObject;
    private static HashMap<String,String> redirectsMap;
    private static Logger logger = Logger.getLogger(Redirects.class.getName());
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");


    private Redirects() {
        // do nothing
    }

    public Redirects getRedirectsObject(){
        if(redirectsObject == null){
            redirectsObject = new Redirects();
            return redirectsObject;
        } else {
            return redirectsObject;
        }
    }


    /**
     * This function will return redirects for a given
     * resource.
     * @param resource : resource for which redirect is required
     * @return Redirect mapping for a resource. Null if there is no resource.
     */
    public String getRedirect(String resource){
        String redirect = null;
        if(redirectsMap == null){
            readRedirectFile();
        }

        if(redirectsMap.get(resource)!= null){
            redirect = redirectsMap.get(resource);
        }

        return redirect;
    }


    /**
     * This function with read redirects file
     * and populates HashMap.
     */
    public void readRedirectFile(){
        String redirectFilePath = rootDirectoryPath+"//redirects//";
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine="";
        int i=1;
        try{

            File redirectsDirectory=new File(redirectFilePath);

            if(redirectsDirectory.isDirectory()) {

                redirectsMap=new HashMap<String,String>();

                for(File redirectsFile:redirectsDirectory.listFiles()){
                    if(redirectsFile.getName().toLowerCase().endsWith(".ttl")){

                        fileReader=new FileReader(redirectsFile);
                        bufferedReader=new BufferedReader(fileReader);

                        while((fileLine=bufferedReader.readLine().trim())!="-1"){
                            String resourceLink=
                                    fileLine.substring(1,fileLine.indexOf(">"));

                            String redirectLink=
                                    fileLine.substring(fileLine.lastIndexOf("<")+1,
                                            fileLine.lastIndexOf(">"));


                            redirectsMap.put(resourceLink,redirectLink);
                        }

                    }
                }
            }
        }
        catch(Exception ex){
            logger.severe(ex.getMessage());
        }
    }
}
