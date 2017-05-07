package extractionPostprocessing.model;


public class Evaluator {

    private int falseNegatives;
    private int falsePositives;
    private int truePositives;
    private int trueNegatives;
    private int totalMappings;


    public Evaluator(int falseNegatives, int falsePositives,
                     int truePositives, int trueNegatives, int totalMappings) {

        this.falseNegatives = falseNegatives;
        this.falsePositives = falsePositives;
        this.truePositives = truePositives;
        this.trueNegatives = trueNegatives;
        this.totalMappings = totalMappings;
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

    public double getPrecision() {

        if ((this.truePositives + this.falsePositives) != 0)
            return (double) this.truePositives / (this.truePositives + this.falsePositives);
        else
            return 0;
    }

    public double getRecall() {

        if ((this.truePositives + this.falseNegatives) != 0)
            return (double) this.truePositives / (this.truePositives + this.falseNegatives);
        else
            return 0;
    }

    public double getF1Measure() {
        double precision = this.getPrecision();
        double recall = this.getRecall();

        if ((precision + recall) != 0)
            return (2 * precision * recall) / (precision + recall);
        else
            return 0;
    }

    public double getAccuracy() {

        if (totalMappings != 0) {
            return (double) truePositives / totalMappings * 100;
        } else {
            return 0;
        }
    }

    public String toString() {
        return "True Positives: " + truePositives + "\n" +
                "Total Mappings: " + totalMappings + "\n" +
                "Accuracy: " + getAccuracy() + "%";
    }
}
