package applications.wikiaStatistics;

import utils.IOoperations;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class allows to calculate and to persist statistics about the gold standard.
 */
public class GoldStandardStatistics {

    public static void main(String[] args) {
        String path = "C:\\Users\\D060249\\IdeaProjects\\ExtractionFromWikia\\additionalFiles\\evaluationFiles";
        GoldStandardStatistics goldStandardStatistics = new GoldStandardStatistics(path);
        goldStandardStatistics.printGoldStandardStatistics();
        goldStandardStatistics.writeGoldStandardStatisticsIntoFile();
    }

    // path to directory where all evaluation files are located
    File evaluationDirectory;

    int numberOfResourcesMappingToNull = 0;
    int numberOfResourcesMappingToDBpedia = 0;
    int numberOfPropertiesMappingToNull = 0;
    int numberOfPropertiesMappingToDBpedia = 0;
    int numberOfClassesMappingToNull = 0;
    int numberOfClassesMappingToDBpedia = 0;
    int numberOfEvaluationFiles = 0;
    private ArrayList<String> linesNotProcessed = new ArrayList<String>();


    /**
     * Constructor
     * @param filePath The path to the directory where all evaluation files are located.
     */
    public GoldStandardStatistics(String filePath) {
        this(new File(filePath));
    }

    /**
     * Constructor
     * @param evaluationDirectory The directory where all evaluation files are located.
     */
    public GoldStandardStatistics(File evaluationDirectory) {
        this.evaluationDirectory = evaluationDirectory;
        if (!evaluationDirectory.isDirectory()) {
            System.out.println("ERROR: Given Evaluation Directory (" + evaluationDirectory.getAbsolutePath() + ") is no directory.");
        }
        calculateGoldStandardStatistics();
    }

    private void calculateGoldStandardStatistics() {
        for (File evaluationFile : evaluationDirectory.listFiles()) {

            BufferedReader reader = null;
            if (evaluationFile.getName().endsWith("ttl")) {
                numberOfEvaluationFiles++;
                System.out.println("Processing file " + evaluationFile.getName());

                try {
                    reader = new BufferedReader(new FileReader(evaluationFile));
                    String line;

                    while ((line = reader.readLine()) != null) {

                        line = line.toLowerCase();

                        if (!line.startsWith("#") || line.equals("") || line.equals(" ") || line.equals("\n")) {
                            // -> line is not a comment

                            if (line.contains("/resource/")) {
                                if (line.contains("<null>")) {
                                    numberOfResourcesMappingToNull++;
                                } else {
                                    numberOfResourcesMappingToDBpedia++;
                                }
                            } else if (line.contains("/property/")) {
                                if (line.contains("<null>")) {
                                    numberOfPropertiesMappingToNull++;
                                } else {
                                    numberOfPropertiesMappingToDBpedia++;
                                }
                            } else if (line.contains("/class/")) {
                                if (line.contains("<null>")) {
                                    numberOfClassesMappingToNull++;
                                } else {
                                    numberOfClassesMappingToDBpedia++;
                                }
                            } else {

                                boolean lineEmpty = true;
                                for(char i : line.toCharArray()){
                                    if(i < 128);
                                    lineEmpty = false;
                                }

                                if(!lineEmpty) {
                                    System.out.println("The following line could not be assigned: ");
                                    System.out.println(line);
                                    linesNotProcessed.add(line);
                                }
                            }
                        }
                    } // end of if line starts with #

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } // end of if evaluation file ends with ttl
        } // end of loop through all evaluation files in directory
    } // end of method calculateGoldStandardStatistics()


    /**
     * Print information about the gold standard on the console.
     */
    public void printGoldStandardStatistics(){

        System.out.println(getResultRepresentationAsString());

    }


    /**
     * Get a String representation of the statistics.
     * @return
     */
    public String getResultRepresentationAsString(){

        StringBuffer buffer = new StringBuffer();

        buffer.append("Total number of mappings: " + getTotalNumberOfMappings()+ "\n" );
        buffer.append("Number of evaluation files: " + numberOfEvaluationFiles+ "\n" );
        buffer.append("\n"+ "\n" );

        buffer.append("Resource Mappings"+ "\n" );
        buffer.append("Number of resource mappings: " + (numberOfResourcesMappingToNull + numberOfResourcesMappingToDBpedia) + "\n" );
        buffer.append("Number of resources mapped to DBpedia: " + numberOfResourcesMappingToDBpedia+ "\n" );
        buffer.append("Number of resources mapped to <null>: " + numberOfResourcesMappingToNull+ "\n" );
        buffer.append("Percentage of resources already in DBpedia: " + ((double) numberOfResourcesMappingToDBpedia / (numberOfResourcesMappingToDBpedia + numberOfResourcesMappingToNull)) + "\n");
        buffer.append("\n"+ "\n" );

        buffer.append("Property Mappings"+ "\n" );
        buffer.append("Number of property mappings: " + (numberOfPropertiesMappingToNull + numberOfPropertiesMappingToDBpedia) + "\n" );
        buffer.append("Number of properties mapped to DBpedia: " + numberOfPropertiesMappingToDBpedia+ "\n" );
        buffer.append("Number of properties mapped to <null>: " + numberOfPropertiesMappingToNull+ "\n" );
        buffer.append("Percentage of properties already in DBpedia: " + ((double) numberOfPropertiesMappingToDBpedia / (numberOfPropertiesMappingToDBpedia + numberOfPropertiesMappingToNull)) + "\n");
        buffer.append("\n"+ "\n" );

        buffer.append("Class Mappings"+ "\n" );
        buffer.append("Number of class mappings: " + (numberOfClassesMappingToNull + numberOfClassesMappingToDBpedia) + "\n" );
        buffer.append("Number of classes mapped to DBpedia: " + numberOfClassesMappingToDBpedia+ "\n" );
        buffer.append("Number of classes mapped to <null>: " + numberOfClassesMappingToNull+ "\n" );
        buffer.append("Percentage of classes already in DBpedia: " + ((double) numberOfClassesMappingToDBpedia / (numberOfClassesMappingToDBpedia + numberOfClassesMappingToNull)) + "\n");
        buffer.append("\n"+ "\n" );

        buffer.append("The following lines could not be processed:"+ "\n" );
        for(String s : linesNotProcessed){
            buffer.append(s + "\n");
        }

        return buffer.toString();
    }


    /**
     * Returns the total number of mappings existing in the gold standard.
     * @return
     */
    public int getTotalNumberOfMappings() {
        return numberOfClassesMappingToNull + numberOfClassesMappingToDBpedia + numberOfPropertiesMappingToNull
                + numberOfPropertiesMappingToDBpedia + numberOfResourcesMappingToNull + numberOfResourcesMappingToDBpedia;

    }


    /**
     * Write the statistics about the gold standard into <root>/statistics
     */
    public void writeGoldStandardStatisticsIntoFile(){
        String pathToRoot = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

        File statisticsDirectory = new File(pathToRoot + "/statistics");
        if(!statisticsDirectory.exists()){
            IOoperations.createDirectory(pathToRoot + "/statistics");
        }

        File f = new File(pathToRoot + "/statistics/goldStandardStatistics.txt");
        IOoperations.writeContentToFile(f, getResultRepresentationAsString());
    }

}
