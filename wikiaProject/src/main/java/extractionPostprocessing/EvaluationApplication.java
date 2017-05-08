package extractionPostprocessing;

import extractionPostprocessing.controller.EntitiesMapping;
import extractionPostprocessing.controller.MappingsEvaluation;
import utils.ExtractionBz2;

import java.io.File;
import java.util.ResourceBundle;


public class EvaluationApplication {

    public static void main(String[] args) {

        // evaluate manual mappings with created mappings for all wikis in root folder
        MappingsEvaluation.evaluateAllMappings();


    }


}
