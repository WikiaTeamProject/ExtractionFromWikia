package extractionPostprocessing.controller;

import extractionPostprocessing.model.EvaluationResult;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 * Test class for {@link MappingEvaluator MappingEvaluator}.
 * The test also covers some functionality of {@Link extractionPostprocessing.model.EvaluationResult EvaluationResult}.
 */
public class MappingEvaluatorTest {


    @BeforeClass
    public static void setUp(){
        // make sure that the fake path to directory exists
        assertTrue(new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory")).isDirectory());
    }

    @Test
    public void evaluateAllMappings() throws Exception {

        MappingEvaluator.evaluateAllMappings();

        // macro average
        assertEquals(MappingEvaluator.getMacroAverageAccuracyInPercent(), 50.0, 0.00000001);
        assertEquals(MappingEvaluator.getMacroAveragePrecisionInPercent(), ((7.0/6.0)/2.0) * 100 , 0.00000001);
        assertEquals(MappingEvaluator.getMacroAverageRecallInPercent(), 50.0, 0.00000001);
        assertEquals(MappingEvaluator.getMacroAverageF1measureInPercent(), (40.0 + (2.0/3.0) * 100.0) / 2.0, 0.00000001);

        // micro average
        assertEquals(MappingEvaluator.getMicroAverageTruePositives(), 5);
        assertEquals(MappingEvaluator.getMicroAverageFalsePositives(), 3);
        assertEquals(MappingEvaluator.getMicroAverageTrueNegatives(), 3);
        assertEquals(MappingEvaluator.getMicroAverageFalseNegatives(), 4);


        assertEquals(MappingEvaluator.getMicroAverageAccuracyInPercent(), (8.0/15.0) * 100, 0.00000001);
        assertEquals(MappingEvaluator.getMicroAveragePrecisionInPercent(), (5.0/8.0) * 100, 0.00000001);
        assertEquals(MappingEvaluator.getMicroAverageRecallInPercent(), (5.0/9.0) * 100, 0.00000001);
        assertEquals(MappingEvaluator.getMicroAverageF1measureInPercent(), ((2.0*(5.0/8.0)*(5.0/9.0)) / ((5.0/8.0)+(5.0/9.0))) * 100.0, 0.00000001);

        // weighted average
        assertEquals(MappingEvaluator.getWeightedOverallAccuracyInPercent(), (40.0/3 + 120.0/3), 0.00000001);
        assertEquals(MappingEvaluator.getWeightedOverallPrecisionInPercent(), (50.0/3+(4.0/6.0)*200/3.0), 0.00000001);
        assertEquals(MappingEvaluator.getWeightedOverallRecallInPercent(), (1.0/9.0) * 100 + (2.0/3.0) * (4.0/6.0) * 100.0, 0.00000001);
        assertEquals(MappingEvaluator.getWeightedOverallF1MeasureInPercent(), (40.0/3.0) + (2.0/3.0) * (2.0/3.0) * 100, 0.00000001);

    }


    @Test
    public void evaluateMappingsForOneWiki() throws Exception {

        // case 1
        EvaluationResult result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/evaluation_test");
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
        result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/test_root/PostProcessedWikis/test_wiki_2");
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