package extractionPostprocessing.model;

/**
 * EvaluationResultSingleWiki is a data structure for mapping evaluation results of a single wiki.
 */
public class EvaluationResultAllWikis {


    // KPIs that are calculated for evaluation


    // weighted average
    public double weightedOverallAccuracyInPercent = 0;
    public double weightedOverallPrecisionInPercent = 0;
    public double weightedOverallRecallInPercent = 0;
    public double weightedOverallF1MeasureInPercent = 0;

    // micro average
    public double microAverageTruePositives = 0;
    public double microAverageFalsePositives = 0;
    public double microAverageTrueNegatives = 0;
    public double microAverageFalseNegatives = 0;
    public double microAverageAccuracyInPercent = 0;
    public double microAveragePrecisionInPercent = 0;
    public double microAverageRecallInPercent = 0;
    public double microAverageF1measureInPercent = 0;

    // macro average
    public double macroAverageAccuracyInPercent = 0;
    public double macroAveragePrecisionInPercent = 0;
    public double macroAverageRecallInPercent = 0;
    public double macroAverageF1measureInPercent = 0 ;

}
