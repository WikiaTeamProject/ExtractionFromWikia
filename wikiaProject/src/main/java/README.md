# SingleProcessApplication
This application combines all subapplications into one single process. This includes:
 - [WikiaStatistics](./wikiaStatistics): Retrieving an overview of all Wikia wikis
 - [WikiaDumpDownload](./wikiaDumpDownload): Downloading existing wikis
 - [Extraction](./extraction): Extracting wikis with the DBpedia extraction framework
 - [ExtractionPostprocessing](./extractionPostprocessing): Creating one mapping file per wiki
 
 To allow for a stable program, prerequisites are checked before running the actual process. Please check before running any process whether all [prerequisites](README.md) are fulfilled.
