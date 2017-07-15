package extractionPostprocessing.controller;

import extractionPostprocessing.model.EvaluationResultAllWikis;
import extractionPostprocessing.model.EvaluationResultSingleWiki;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 * Test class for {@link MappingEvaluator MappingEvaluator}.
 * The test also covers some functionality of {@Link extractionPostprocessing.model.EvaluationResultSingleWiki EvaluationResultSingleWiki}.
 */
public class MappingEvaluatorTest {


    @BeforeClass
    public static void setUp(){
        // make sure that the fake path to directory exists
        assertTrue(new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory")).isDirectory());
    }

    @Test
    public void evaluateAllMappings() throws Exception {

        EvaluationResultAllWikis result = MappingEvaluator.evaluateAllMappings(MappingEvaluator.EvaluationObject.RESOURCES, false);

        // macro average
        assertEquals(result.macroAverageAccuracyInPercent, 50.0, 0.00000001);
        assertEquals(result.macroAveragePrecisionInPercent, ((7.0/6.0)/2.0) * 100 , 0.00000001);
        assertEquals(result.macroAverageRecallInPercent, 50.0, 0.00000001);
        assertEquals(result.macroAverageF1measureInPercent, (40.0 + (2.0/3.0) * 100.0) / 2.0, 0.00000001);

        // micro average
        assertEquals(result.microAverageTruePositives, 5, 0);
        assertEquals(result.microAverageFalsePositives, 3, 0);
        assertEquals(result.microAverageTrueNegatives, 3, 0);
        assertEquals(result.microAverageFalseNegatives, 4, 0);


        assertEquals(result.microAverageAccuracyInPercent, (8.0/15.0) * 100, 0.00000001);
        assertEquals(result.microAveragePrecisionInPercent, (5.0/8.0) * 100, 0.00000001);
        assertEquals(result.microAverageRecallInPercent, (5.0/9.0) * 100, 0.00000001);
        assertEquals(result.microAverageF1measureInPercent, ((2.0*(5.0/8.0)*(5.0/9.0)) / ((5.0/8.0)+(5.0/9.0))) * 100.0, 0.00000001);

        // weighted average
        assertEquals(result.weightedOverallAccuracyInPercent, (40.0/3 + 120.0/3), 0.00000001);
        assertEquals(result.weightedOverallPrecisionInPercent, (50.0/3+(4.0/6.0)*200/3.0), 0.00000001);
        assertEquals(result.weightedOverallRecallInPercent, (1.0/9.0) * 100 + (2.0/3.0) * (4.0/6.0) * 100.0, 0.00000001);
        assertEquals(result.weightedOverallF1MeasureInPercent, (40.0/3.0) + (2.0/3.0) * (2.0/3.0) * 100, 0.00000001);

    }


    @Test
    public void evaluateMappingsForOneWiki() throws Exception {

        // case 1
        EvaluationResultSingleWiki result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/evaluation_test", MappingEvaluator.EvaluationObject.RESOURCES);
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 1);
        assertTrue(result.getTruePositives() == 1);
        assertTrue(result.getTrueNegatives() == 1);
        assertTrue(result.getPositives() == 3);
        assertTrue(result.getNegatives() == 2);
        assertTrue(result.getRecallInPercent() == (1.0/3) * 100);
        assertTrue(result.getPrecisionInPercent() == (1.0/2) * 100); // = 50
        assertTrue(result.getAccuracyInPercent() == (2.0/5) * 100);
        assertTrue(result.getF1MeasureInPercent() == ((2.0 * 0.5 * (1.0/3))/(0.5 + 1.0/3))*100); // = 40.0


        // case 2
        result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/test_root/PostProcessedWikis/test_wiki_2", MappingEvaluator.EvaluationObject.RESOURCES);
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 2);
        assertTrue(result.getTruePositives() == 4);
        assertTrue(result.getTrueNegatives() == 2);
        assertTrue(result.getPositives() == 6);
        assertTrue(result.getNegatives() == 4);
        assertTrue(result.getRecallInPercent() == (4.0/6) * 100);
        assertTrue(result.getPrecisionInPercent() == (4.0/6) * 100);
        assertTrue(result.getAccuracyInPercent() == (6.0/10) * 100);
        assertTrue(result.getF1MeasureInPercent() == ((2.0 * (4.0/6) * (4.0/6))/((4.0/6) + (4.0/6)))*100); // = ( 2.0 / 3 ) * 100 = 66.66666667

    }



}