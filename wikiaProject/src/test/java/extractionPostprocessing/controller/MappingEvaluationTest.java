package extractionPostprocessing.controller;

import extractionPostprocessing.model.EvaluationResult;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link extractionPostprocessing.controller.MappingEvaluation MappingEvaluation}.
 * The test also covers some functionality of {@Link extractionPostprocessing.model.EvaluationResult EvaluationResult}.
 */
public class MappingEvaluationTest {

    @Test
    public void evaluateMappingsForOneWiki() throws Exception {
        EvaluationResult result = MappingEvaluation.evaluateMappingsForOneWiki("./src/test/test_files/evaluation_test");
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 1);
        assertTrue(result.getTruePositives() == 1);
        assertTrue(result.getTrueNegatives() == 1);
        assertTrue(result.getPositives() == 3);
        assertTrue(result.getNegatives() == 2);
        assertTrue(result.getRecallInPercent() == (1.0/3) * 100);
        assertTrue(result.getPrecisionInPercent() == (1.0/2) * 100);
        assertTrue(result.getAccuracyInPercent() == (2.0/5) * 100);
        assertTrue(result.getF1MeasureInPercent() == ((2.0 * 0.5 * (1.0/3))/(0.5 + 1.0/3))*100);
    }

}