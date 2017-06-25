package extractionPostprocessing.controller;

import extractionPostprocessing.model.EvaluationResult;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Test class for {@link extractionPostprocessing.controller.MappingEvaluation MappingEvaluation}.
 */
public class MappingEvaluationTest {

    @Test
    public void evaluateMappingsForOneWiki() throws Exception {
        EvaluationResult result = MappingEvaluation.evaluateMappingsForOneWiki("./src/test/test_files/evaluation_test");
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 1);
        assertTrue(result.getTruePositives() == 1);
        assertTrue(result.getTrueNegatives() == 1);
        assertTrue(result.getRecall() == (1.0/3) * 100);
        assertTrue(result.getPrecision() == (1.0/2) * 100);
        assertTrue(result.getAccuracy() == (2.0/5) * 100);
        assertTrue(result.getF1Measure() == ((2.0 * 0.5 * (1.0/3))/(0.5 + 1.0/3))*100);
    }

}