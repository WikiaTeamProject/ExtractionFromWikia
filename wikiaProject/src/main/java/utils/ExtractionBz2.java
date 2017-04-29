package utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Jan Portisch on 29.04.2017.
 */
public class ExtractionBz2 {

    private static Logger logger = Logger.getLogger(ExtractionBz2.class.getName());


    public static void extract(String pathToFileToExtract, String pathToNewDirectory){
        try {
            FileInputStream in = new FileInputStream(pathToFileToExtract);
            FileOutputStream out = new FileOutputStream(pathToNewDirectory);
            BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
            final byte[] buffer = new byte[8192];
            int n = 0;
            while (-1 != (n = bzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            out.close();
            bzIn.close();

        }catch (IOException ioe){
            logger.severe(ioe.toString());
        }
    }


}
