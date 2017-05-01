package extractionPostprocessing.model;

/**
 * Created by Samresh Kumar on 5/1/2017.
 */
public class Evaluator {

    private double falseNegatives;
    private double falsePositives;
    private double truePositives;
    private double trueNegatives;

    public Evaluator(double falseNegatives,double falsePositives,
                     double truePositives,double trueNegatives){

        this.falseNegatives=falseNegatives;
        this.falsePositives=falsePositives;
        this.truePositives=truePositives;
        this.trueNegatives=trueNegatives;

    }

    public double getFalseNegatives(){
        return this.falseNegatives;
    }

    public double getFalsePositives(){
        return this.falsePositives;
    }

    public double getTruePositives(){
        return this.truePositives;
    }
    public double getTrueNegatives(){
        return this.trueNegatives;
    }

    public void setFalseNegatives(double falseNegatives ){
        this.falseNegatives=falseNegatives;
    }

    public void setFalsePositives(double falsePositives){
        this.falsePositives=falsePositives;
    }

    public void setTruePositives(double truePositives){
        this.truePositives=truePositives;
    }

    public void setTrueNegatives(double trueNegatives){
        this.trueNegatives=trueNegatives;
    }

    public double getPrecision(){
        if((this.truePositives+this.falsePositives)==0)
            return this.truePositives;
        else
            return this.truePositives/(this.truePositives+this.falsePositives);
    }

    public double getRecall(){
        if((this.truePositives+this.falseNegatives)==0)
            return this.truePositives;
        else
            return this.truePositives/(this.truePositives+this.falseNegatives);
    }

    public double fMeasure(){
        double precision=this.getPrecision();
        double recall=this.getRecall();

        if((precision+recall)==0)
            return (2*precision*recall);
        else
            return (2*precision*recall)/(precision+recall);

    }

}
