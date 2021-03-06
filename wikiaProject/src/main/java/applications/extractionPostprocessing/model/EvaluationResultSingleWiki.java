package applications.extractionPostprocessing.model;

/**
 * EvaluationResultSingleWiki is a data structure for mapping evaluation results of a single wiki.
 */
public class EvaluationResultSingleWiki {

    // initialize values with zero
    private int falseNegatives = 0;
    private int falsePositives = 0;
    private int truePositives = 0;
    private int trueNegatives = 0;
    private int totalMappings = 0;


    /**
     * Constructor
     * @param falseNegatives
     * @param falsePositives
     * @param truePositives
     * @param trueNegatives
     */
    public EvaluationResultSingleWiki(int falseNegatives, int falsePositives,
                                      int truePositives, int trueNegatives) {

        this.falseNegatives = falseNegatives;
        this.falsePositives = falsePositives;
        this.truePositives = truePositives;
        this.trueNegatives = trueNegatives;
        this.totalMappings = trueNegatives+truePositives+falseNegatives+falsePositives;
    }

    public int getFalseNegatives() {
        return this.falseNegatives;
    }

    public int getFalsePositives() {
        return this.falsePositives;
    }

    public int getTruePositives() {
        return this.truePositives;
    }

    public int getTrueNegatives() {
        return this.trueNegatives;
    }

    public void setFalseNegatives(int falseNegatives) {
        this.falseNegatives = falseNegatives;
    }

    public void setFalsePositives(int falsePositives) {
        this.falsePositives = falsePositives;
    }

    public void setTruePositives(int truePositives) {
        this.truePositives = truePositives;
    }

    public void setTrueNegatives(int trueNegatives) {
        this.trueNegatives = trueNegatives;
    }

    public int getTotalMappings() {
        return totalMappings;
    }

    public void setTotalMappings(int totalMappings) {
        this.totalMappings = totalMappings;
    }

    public double getPrecisionInPercent() {

        if ((this.truePositives + this.falsePositives) != 0)
            return ((double) this.truePositives / (this.truePositives + this.falsePositives)) * 100;
        else
            return 0;
    }


    public double getRecallInPercent() {

        if ((this.truePositives + this.falseNegatives) != 0)
            return ((double) this.truePositives / (this.truePositives + this.falseNegatives)) * 100;
        else
            return 0;
    }

    public double getF1MeasureInPercent() {
        double precision = this.getPrecisionInPercent();
        double recall = this.getRecallInPercent();

        if ((precision + recall) != 0)
            return ((2 * (precision/100.0) * (recall/100.0) / ((precision/100.0) + (recall/100.0))) * 100);
        else
            return 0;
    }

    public double getAccuracyInPercent() {
        if (totalMappings != 0) {
            return ((double) (truePositives + trueNegatives) / totalMappings) * 100;
        } else {
            return 0;
        }
    }

    /**
     * All negative entries, i.e. FP + TN.
     * @return
     */
    public int getNegatives(){
        return falsePositives + trueNegatives;
    }

    /**
     * All positive entries, i.e. TP + FN.
     * @return
     */
    public int getPositives(){
        return truePositives + falseNegatives;
    }

    public String toString() {
        return "True Positives: " + truePositives + "\n" +
                "True Negatives: " + trueNegatives + "\n" +
                "False Positives: " + falsePositives + "\n" +
                "False Negatives: " + falseNegatives + "\n" +
                "Total Mappings: " + totalMappings + "\n" +
                "Accuracy: " + this.getAccuracyInPercent() + "%\n" +
                "Precision: " + this.getPrecisionInPercent() + "%\n" +
                "Recall: " + this.getRecallInPercent() + "%\n" +
                "F1-Measure: " + this.getF1MeasureInPercent() + "%";
    }



}
