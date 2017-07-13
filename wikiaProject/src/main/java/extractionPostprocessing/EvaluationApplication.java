package extractionPostprocessing;

import extractionPostprocessing.controller.MappingEvaluator;


/**
 * This class evaluates all mappings.
 * It does not need to be executed for productive purposes.
 */
public class EvaluationApplication {

    public static void main(String[] args) {

        // evaluate manual mappings with created mappings for all wikis in root folder
        MappingEvaluator.evaluateAllMappings();

    }
}
