package extractionPostprocessing;

import extractionPostprocessing.controller.MappingsEvaluation;


public class EvaluationApplication {

    public static void main(String[] args) {

        // evaluate manual mappings with created mappings for all wikis in root folder
        MappingsEvaluation.evaluateAllMappings();


    }


}
