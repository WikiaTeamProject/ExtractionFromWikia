package extractionPostprocessing.model;


public class Evaluator {

    private int falseNegatives;
    private int falsePositives;
    private int truePositives;
    private int trueNegatives;


    public Evaluator(int falseNegatives,int falsePositives,
                     int truePositives,int trueNegatives){

        this.falseNegatives = falseNegatives;
        this.falsePositives = falsePositives;
        this.truePositives = truePositives;
        this.trueNegatives = trueNegatives;

    }

    public int getFalseNegatives(){
        return this.falseNegatives;
    }

    public int getFalsePositives(){
        return this.falsePositives;
    }

    public int getTruePositives(){
        return this.truePositives;
    }

    public int getTrueNegatives() {
        return this.trueNegatives;
    }

    public void setFalseNegatives(int falseNegatives){
        this.falseNegatives = falseNegatives;
    }

    public void setFalsePositives(int falsePositives){
        this.falsePositives = falsePositives;
    }

    public void setTruePositives(int truePositives){
        this.truePositives = truePositives;
    }

    public void setTrueNegatives(int trueNegatives){
        this.trueNegatives = trueNegatives;
    }

    public double getPrecision() {
        if ((this.truePositives + this.falsePositives) == 0)
            return this.truePositives;
        else
            return this.truePositives / (this.truePositives + this.falsePositives);
    }

    public double getRecall() {
        if ((this.truePositives + this.falseNegatives) == 0)
            return this.truePositives;
        else
            return this.truePositives / (this.truePositives + this.falseNegatives);
    }

    public double fMeasure() {
        double precision = this.getPrecision();
        double recall = this.getRecall();

        if ((precision+recall) == 0)
            return (2 * precision * recall);
        else
            return (2 * precision * recall) / (precision + recall);

    }

}
