### Extraction Postprocessing Application
Before running `ExtractionPostprocessingApplication`, make sure that you ran all required
processes before (i.e. you successfully extracted all wikis you want to postprocess now).
There are other applications provided to do just that and to get here (`WikiaDumpDownloadApplication`,
`ExtractionApplication`).

### Mapping Evaluation
When executing the program `EvaluationApplication` make sure that you previously ran the 
process with `includeNullMappings = true` in the `config.properties` file (default: false).
You have to place the evaluation file for each wiki within the wiki folder in `postProcessedWikis`.
