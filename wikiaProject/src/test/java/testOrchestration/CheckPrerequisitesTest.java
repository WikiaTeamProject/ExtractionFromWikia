package testOrchestration;

import org.junit.Test;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;

/**
 * This class tests whether the required files are available.
 */
public class CheckPrerequisitesTest {

    /**
     * Making sure that there qre the required DBpedia files in the test directory
     */
    @Test
    public void checkPrerequisites(){
        String rootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

        File resourceDirectory = new File(rootDirectory + "/resources/ontology");
        boolean existsIndicator = false;
        System.out.println("Checking whether the ontology file exists in the test root directory...");
        for(File f : resourceDirectory.listFiles()){
            if(f.getName().endsWith(".nt")){
                existsIndicator = true;
                break;
            }
        }
        if(existsIndicator) {
            System.out.println("File found.");
        } else {
            System.out.println("Please make sure that there is an ontology file in the test-root-directory.");
            // let the test fail:
            assertTrue(existsIndicator);
        }
        System.out.println("\n");

        resourceDirectory = new File(rootDirectory + "/resources/pageids");
        existsIndicator = false;
        System.out.println("Checking whether the pageids file exists in the test root directory...");
        for(File f : resourceDirectory.listFiles()){
            if(f.getName().endsWith(".ttl")){
                existsIndicator = true;
                break;
            }
        }
        if(existsIndicator) {
            System.out.println("File found.");
        } else {
            System.out.println("Please make sure that there is a pageid file in the test-root-directory.");
            // let the test fail:
            assertTrue(existsIndicator);
        }
        System.out.println("\n");

        resourceDirectory = new File(rootDirectory + "/resources/properties");
        existsIndicator = false;
        System.out.println("Checking whether the properties file exists in the test root directory...");
        for(File f : resourceDirectory.listFiles()){
            if(f.getName().endsWith(".ttl")){
                existsIndicator = true;
                break;
            }
        }
        if(existsIndicator) {
            System.out.println("File found.");
        } else {
            System.out.println("Please make sure that there is a properties file in the test-root-directory.");
            // let the test fail:
            assertTrue(existsIndicator);
        }
        System.out.println("\n");

        resourceDirectory = new File(rootDirectory + "/resources/redirects");
        existsIndicator = false;
        System.out.println("Checking whether the redirects file exists in the test root directory...");
        for(File f : resourceDirectory.listFiles()){
            if(f.getName().endsWith(".ttl")){
                existsIndicator = true;
                break;
            }
        }
        if(existsIndicator) {
            System.out.println("File found.");
        } else {
            System.out.println("Please make sure that there is a redirects file in the test-root-directory.");
            // let the test fail:
            assertTrue(existsIndicator);
        }
    }

}
