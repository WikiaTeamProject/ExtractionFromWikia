package applications.wikiaStatistics;

import applications.wikiaStatistics.controller.GoldStandardStatistics;


/**
 * This applications compiles information about the gold standard.
 * Please note that all evaluation files have to be in one directory.
 * You have to define the directory in the path variable below.
 * You will find the evaluation result saved as <root>/statistics/goldStandardStatistics.txt
 */
public class GoldStandardStatisticsApplication {

    public static void main(String[] args) {
        String path = "C:\\Users\\D060249\\Documents\\Mannheim\\Semester 2\\Team Project\\Evaluation Files";
        GoldStandardStatistics goldStandardStatistics = new GoldStandardStatistics(path);
        goldStandardStatistics.printGoldStandardStatistics();
        goldStandardStatistics.writeGoldStandardStatisticsIntoFile();
    }

}
