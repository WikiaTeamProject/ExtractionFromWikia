package extractionPostprocessing.controller;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by D060249 on 17.06.2017.
 */
public class RedirectProcessorTest {

    private static final String TEST_DIRECTORY_PATH = "./src/test/test_files/redirect_processor_test";
    private static final String COPY_OF_TEST_DIRECTORY_PATH = "./src/test/test_files/redirect_processor_test_2";

    @BeforeClass
    public static void setup(){

        File testDirectoryFile = new File(TEST_DIRECTORY_PATH);
        File copyOfTestDirectoryFile = new File(COPY_OF_TEST_DIRECTORY_PATH);

        try {
            System.out.println(new java.io.File(".").getCanonicalPath());
            FileUtils.copyDirectory(testDirectoryFile, copyOfTestDirectoryFile);
            for(File f : copyOfTestDirectoryFile.listFiles()){
                if(f.getName().endsWith("_solution")){
                    f.delete();
                }
            }

        } catch (IOException ioe){
            System.out.println("Could not execute Test.");
            System.out.println(ioe.toString());
        }
    }

    @Test
    public void executeRedirects() throws Exception {
        File testDirectory = new File(COPY_OF_TEST_DIRECTORY_PATH);
        RedirectProcessor rp = new RedirectProcessor(testDirectory);
        rp.executeRedirects();
        System.out.println(testDirectory.listFiles().length);

        // compare result files with solution
        for (File f : testDirectory.listFiles()) {
            File solution = new File("./src/test/test_files/redirect_processor_test/" + f.getName() + "_solution");
            if(!solution.exists()){
                System.out.println("Test file not available. Test will fail.");
            }
            Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(f, solution, "utf-8") );
        }

    }

    @AfterClass
    public static void cleanup(){
        try {
            File testDirectory = new File(COPY_OF_TEST_DIRECTORY_PATH);

            for(File f : testDirectory.listFiles()){
                f.delete();
            }

            FileUtils.deleteDirectory(testDirectory);
        } catch (IOException ioe){
            System.out.println("There was a problem cleaning up test files.");
            System.out.println(ioe.toString());
        }
    }

}