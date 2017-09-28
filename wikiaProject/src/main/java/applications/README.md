# Applications
Several applications are part of this project which deal with different implementation parts.
This includes:
 - [WikiaStatistics](wikiaStatistics): Retrieving an overview of all Wikia wikis
 - [WikiaDumpDownload](wikiaDumpDownload): Downloading existing wikis
 - [Extraction](extraction): Extracting wikis with the DBpedia applications.extraction framework
 - [ExtractionPostprocessing](extractionPostprocessing): Creating one mapping file per wiki
 - [WikiaDumpRequester](wikiaDumpRequester): Requesting dumps to get the newest content (deprecated, not working anymore due to changes by Wikia)

All applications can be either run on their own in the listed order or directly all together within 
a [single process](./SingleProcessAllWikisApplication.java).
If you just want to see the output of one or e.g. a specific list of wikis, you can execute the [single process application for specific wikis](./SingleProcessSpecificWikisApplication.java). 

To allow for a stable program, prerequisites are checked before running the actual processes. 
Please check before running any process whether all [prerequisites](../../README.md) are fulfilled.
