# ExtractionFromWikia
This is a student project of the Data and Web Science (DWS) group of the University of Mannheim.
The overall goal is to create a knowledge graph out of the [fandom (a.k.a. Wikia) wiki farm](http://www.wikia.com) and 
link it to [DBpedia](http://wiki.dbpedia.org).<br/>
The program implemented here determines all available wiki dumps on wikia, downloads them, mass-processes them using 
the [DBpedia Extraction Framework](https://github.com/dbpedia/applications.extraction-framework), postprocesses the 
output and maps resources, properties and classes to DBpedia. 

Furthermore, it provides functionality to compile various statistics. A [gold standard](./additionalFiles/evaluationFiles) 
for evaluating the performance of mappers including several mapping files is also available. 
The resulting knowledge graph is published [online](http://dbkwik.webdatacommons.org/).

## Goals of this Project
The goal of this project is to create a linked open data dataset.
The tools published here download all available wikis from Wikia (a.k.a. fandom), extract them to create triples and 
map those triples to existing DBpedia resources. 

## Technical Prerequisites
There are some prerequisites that must be fulfilled in order to be able to run the program.
- Java has to be installed
- internet connectivity has to be available throughout the whole process 
- at least 8 gigabytes of RAM have to be allocated to the JVM
- at least 100 gigabytes freely available disk space


## About this Guide
This file represents the main guide of the project. It comprises all information required to run the process but 
will also give you guidance if you are interested in implementation details. You will find more `README.md` files 
within some subfolders. Those describe implementation details. You do not have to read them if you are not interested
in the implementation. This guide also contains links to directories or other webpages so that you can find the place 
where the discussed subject is located. 


## How to execute the Program?
Create the following directory structure (you can name `root_directory` whatever you like, but the other directory 
names have to match):
```
root_directory
+---resources
|      +---pageids
|      +---redirects
|      +---ontology
|      +---properties
```

Go to the DBpedia download page, download the files listed below and decompress them. Put the decompressed files into the specified directory:
- DBpedia Ontology (nt) → `ontology`
- Page Ids (ttl) → `pageids`
- Rransitive Redirects (ttl) → `redirects`
- Infobox Property Definitions (ttl) → `properties`

You can choose whatever version you like, however, if you want to use the provided gold standard for evaluation, go for version [2016-10](http://wiki.dbpedia.org/downloads-2016-10).

Please copy the [sample properties file](./additionalFiles/configFile/config.properties) directly into the [resources folder](/wikiaProject/src/main/resources) and adjust it. 
Have a look at the [detailed description](./additionalFiles/configFile/README.md) of all variables which need to be specified in the properties file.

After successfully running the program, you will find the postprocessed wikis in `root_directory/postProcessedWikis`.


## Implementation Details

### General Structure and Implementation Documentation
The project is mainly implemented in Java. The main process is divided in sub-processes which are organized in folders 
(= packages in Java). Within those folders you will find additional `README.md` files which further explain the 
particular module from a technical perspective. It is not required to read those if you are not interested in the 
implementation. Sometimes, packages contain an application which allow to execute only a module. If you are interested
in executing only a part of the process, this will save you time but note that those applications usually require
that you executed other applications beforehand at least once. All code is documented using JavaDoc.


### Applications 
This project includes several applications which can either be run in a combined way within the [single process](./wikiaProject/src/main/java/applications/SingleProcessApplication.java) or each application by itself which requires some more knowledge.
These are all existing applications including a short description:
 - [WikiaStatistics](./wikiaProject/src/main/java/applications/wikiaStatistics): Retrieving an overview of all Wikia wikis.
 - [WikiaDumpDownload](./wikiaProject/src/main/java/applications/wikiaDumpDownload/): Downloading existing wiki dumps from Wikia.
 - [Extraction](./wikiaProject/src/main/java/applications/extraction/): Extracting wikis with the DBpedia applications.extraction framework.
 - [ExtractionPostprocessing](./wikiaProject/src/main/java/applications/extractionPostprocessing): Creating one mapping file per wiki.

 To allow for a stable program, prerequisites are checked before running the actual process. Please check before running any process whether all mentioned prerequisites are fulfilled.

### File Structure
In the `config.properties` file, you specify a root directory. For a regular program run-through this is already sufficient. If you want to extend the coding or execute only parts of the complete chain of commands, you have to know about the file structure.

```
root_directory
+---resources
|      +---pageids
|      +---redirects
|      +---ontology
|      +---properties
+---downloadedWikis
|     +---7z
|     +---gz
|     +---decompressed
+---dbPediaExtractionFormat
+---postProcessedWikis
+---statistics
```

The program performs a lot of file operations. All of those file operations are handled within the `root_directory` that you specify in the `config.properties` file.
- The `resources` directory contains different files from DBpedia. This folder requires user interaction: the user has to put the files in the directory (as specified in "How to execute the Program?").
   - The `pageids` directory should contain a TTL file with all page ids of DBpedia. This is required for some mappers to work.
   - The `redirects` directory should contain a TTL file with all redirects of DBpedia. This is required for some mappers to work.
   - The `ontology` directory should contain a TTL file with all ontologies of DBpedia. This is required for some mappers to work.
   - The `properties` mapper should contain a TTL file with all properties of DBpedia. This is required for some mappers to work.
- The `downloadedWikis` directory contains plain dumps from wikia.
     - The `7z` directory contains all wikis that were downloaded in the 7z format.
     - The `gz` directory contains all wikis that were downloaded in the gz format.
     - The `decompressed` directory contains all wikis from the `7z` and `gz` folder but in a decompressed format.
- The `dbPediaExtractionFormat` contains the decompressed wiki dumps that are following a file structure required for the DBpedia extractor to work.
- The `PostProcessedWikis` directory contains all wikis in their final postprocessed form. After successfully running the program, the user should find the final output here. 
- The `statistics` directory contains various statistics files that are created throughout the process.


### Code Quality and Unit Tests
All tests can be found in the [Test Directory](.wikiaProject/src/test). Please note that the test coverage is not 100%.

