package utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Test for class {@Link utils.FileOperations}.
 */

public class FileOperationsTest {

    @Test
    public void getFileNameFromPath() throws Exception {
        assertEquals(FileOperations.getFileNameFromPath(new File("./src/test/test_files/evaluation_test/evaluation.ttl")), "evaluation.ttl");
        File f = new File("./src/test/test_files/evaluation_test/evaluation.ttl");
        assertEquals(FileOperations.getFileNameFromPath(f), "evaluation.ttl");
    }

}