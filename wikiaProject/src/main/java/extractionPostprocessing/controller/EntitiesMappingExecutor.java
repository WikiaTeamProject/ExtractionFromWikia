package extractionPostprocessing.controller;

import extractionPostprocessing.model.MapperInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for the creation of the mappings files.
 * Given a mapper, this class performs the mapping for all wikis.
 */
public class EntitiesMappingExecutor {

    private static Logger logger = Logger.getLogger(EntitiesMappingExecutor.class.getName());
    private MapperInterface mapper;

    public EntitiesMappingExecutor(MapperInterface mapper) {
        this.mapper = mapper;
    }

    /**
     * Loops over the root directory and creates the mapping files.
     */
    public void createMappingFilesForAllWikis() {

        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/PostProcessedWikis";
        File root = new File(pathToRootDirectory);

        if (root.isDirectory()) {
            for (File directory : root.listFiles()) {
                if (directory.isDirectory()) {
                    mapper.createMappingFileForSingleWiki(directory);
                }
            }
        }
    }


    public MapperInterface getMapper() {
        return mapper;
    }

    public void setMapper(MapperInterface mapper) {
        this.mapper = mapper;
    }
}
