package wikiaStatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by D060249 on 26.07.2017.
 */
public class GoldStandardStatistics {

    public static void main(String[] args) {
        String path = "C:\\Users\\D060249\\IdeaProjects\\ExtractionFromWikia\\additionalFiles\\evaluationFiles";
        GoldStandardStatistics goldStandardStatistics = new GoldStandardStatistics(path);
        goldStandardStatistics.calculateGoldStandardStatistics();
    }

    // path to directory where all evaluation files are located
    String evaluationFilePath;
    File evaluationDirectory;

    int numberOfResourcesMappingToNull = 0;
    int numberOfResourcesMappingToDBpedia = 0;
    int numberOfPropertiesMappingToNull = 0;
    int numberOfPropertiesMappingToNDBpedia = 0;
    int numberOfClassesMappingToNull = 0;
    int numberOfClassesMappingToDBpedia = 0;

    public GoldStandardStatistics(String filePath) {
        this(new File(filePath));
    }

    public GoldStandardStatistics(File evaluationDirectory) {
        this.evaluationDirectory = evaluationDirectory;
        if (!evaluationDirectory.isDirectory()) {
            System.out.println("ERROR: Given Evaluation Directory (" + evaluationDirectory.getAbsolutePath() + ") is no directory.");
        }
    }

    public void calculateGoldStandardStatistics() {
        for (File evaluationFile : evaluationDirectory.listFiles()) {

            BufferedReader reader = null;
            if (evaluationFile.getName().endsWith("ttl")) {
                System.out.println("Processing file " + evaluationFile.getName());

                try {
                    reader = new BufferedReader(new FileReader(evaluationFile));
                    String line;

                    while ((line = reader.readLine()) != null) {

                        line = line.toLowerCase();

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
                                numberOfPropertiesMappingToNDBpedia++;
                            }
                        } else if (line.contains("/class/")){
                            if (line.contains("<null>")){
                                numberOfClassesMappingToNull++;
                            } else {
                                numberOfClassesMappingToDBpedia++;
                            }
                        } else {
                            System.out.println("The following line could not be assigned: ");
                            System.out.println(line);
                        }
                    }


                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }


            }
        }
    }
}
