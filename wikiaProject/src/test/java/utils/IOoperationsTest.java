package utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Test for class {@Link utils.IOoperations}.
 */

public class IOoperationsTest {

    @Test
    public void getFileNameFromPath() throws Exception {
        assertEquals(IOoperations.getFileNameFromPath(new File("./src/test/test_files/evaluation_test/evaluation.ttl")), "evaluation.ttl");
        File f = new File("./src/test/test_files/evaluation_test/evaluation.ttl");
        assertEquals(IOoperations.getFileNameFromPath(f), "evaluation.ttl");
    }

}