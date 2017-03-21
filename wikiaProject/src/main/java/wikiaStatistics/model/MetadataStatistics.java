package wikiaStatistics.model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MetadataStatistics {

    private HashMap<String, Integer> languageCounts;
    private int numberOfArticles;
    private int numberOfPages;

    private Logger logger = Logger.getLogger(getClass().getName());


    public MetadataStatistics() {
        this.languageCounts = initializeHashMap();
    }


    public HashMap<String, Integer> getLanguageCounts() {
        return languageCounts;
    }

    public void setLanguageCounts(HashMap<String, Integer> languageCounts) {
        this.languageCounts = languageCounts;
    }

    public int getNumberOfArticles() {
        return numberOfArticles;
    }

    public void setNumberOfArticles(int numberOfArticles) {
        this.numberOfArticles = numberOfArticles;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "MetadataStatistics: \n" +
                "Languages = " + languageCounts + "\n" +
                "Number of articles = " + numberOfArticles + "\n" +
                "Number of pages = " + numberOfPages;
    }

    private HashMap<String, Integer> initializeHashMap() {
        BufferedReader bufferedReader;
        String readLine;
        String[] tokens;
        HashMap<String, Integer> languageCounts = new HashMap<String, Integer>();

        try {
            bufferedReader = new BufferedReader(new FileReader("./wikiaProject/src/main/resources/wikiaLanguageCodes.csv"));

            while ((readLine = bufferedReader.readLine()) != null) {
                tokens = readLine.split(";");

                if (!tokens[0].isEmpty()) {
                    languageCounts.put(tokens[0], 0);
                }
            }

        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "HashMap has not been initialized.", ioe);
        }

        return languageCounts;
    }

    public void limitTopLanguages(int occurrences) {
        HashMap<String, Integer> limitedLanguages = new HashMap<String, Integer>();

        for (Map.Entry<String, Integer> entry : this.getLanguageCounts().entrySet()) {

            if(entry.getValue() >= occurrences) {
                // add entries with at minimum specified number of occurrences
                limitedLanguages.put(entry.getKey(), entry.getValue());
            }

        }

        this.setLanguageCounts(limitedLanguages);
    }
}
