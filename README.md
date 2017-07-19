# ExtractionFromWikia
This is a student project of the University of Mannheim. 

## Goals of this Project
The goal of this project is to create a linked open data dataset.
The tools publishe here download all available wikis from wikia (a.k.a. fandom), extract them to create triples and map those triples to existing DBpedia resources. 

## Technical Prerequisites
There are some prerequisites that must be fulfilled in order to be able to run the program.
- Java has to be installed
- internet connectivity has to be available throughout the whole process 
- at least 8 gigabytes of RAM have to be allocated to the JVM
- at least 100 gigabytes freely available disk space

## How to execute the Program?
Create the following directory structure (you can name `root_directory` whatever you like, but the other directory names have to match):
```
root_directory
+---resources
|      +---pageids
|      +---redirects
|      +---ontology
|      +---properties
```

Go to the DBpedia download page, download the following files and put them into the specified directory:
- DBpedia Ontology (nt) → `ontology`
- Page Ids (ttl) → `pageids`
- Rransitive Redirects (ttl) → `redirects`
- Infobox Property Definitions (ttl) → `properties`

You can choose whatever version you like, however, if you want to use the provided gold standard for evaluation, you should go for version 2016-10 (http://wiki.dbpedia.org/downloads-2016-10).

<todo: add more>

After successfully running the program, you will find the postprocessed wikis in `root_directory/postProcessedWikis`.


## Implementation Details

### File Structure
In the `config.properties` file, you specify a root directory. For a regular program run-through this is already sufficient. If you want to extend the coding or execute only parts of the complete chain of commands, you have to know about the file structure.

```
root_directory
+---downloadedWikis
|     +---7z
|     +---gz
|     +---decompressed
+---dbPediaExtractionFormat
+---resources
|      +---pageids
|      +---redirects
|      +---ontology
|      +---properties
+---postProcessedWikis
+---statistics
```

The program performs a lot of file operations. All of those file operations are handled within the `root_directory` that you specify in the `config.properties file`.
- The `downloadedWikis` directory contains plain dumps from wikia
     - The `7z` directory contains all wikis that were downloaded in the 7z format.
     - The `gz` directory contains all wikis that were downloaded the gz format.
     - The `decompressed` directory contains all wikis from the `7z` and `gz` folder but in a decompressed format.
- The `dbPediaExtractionFormat` contains the decompressed wiki dumps that are following a file structure required for the dbPedia extractor to work.
- The `resources` directory contains different files from DBpedia. This folder requires user interaction: the user has to put the files in the directory.
   - The `pageids` directory should contain a TTL file with all page ids of DBpedia. This is required for some mappers to work.
   - The `redirects` directory should contain a TTL file with all redirects of DBpedia. This is required for some mappers to work.
   - The `ontology` directory should contain a TTL file with all ontologies of DBpedia. This is required for some mappers to work.
   - The `properties` mapper should contain a TTL file with all properties of DBpedia. This is required for some mappers to work.
- The `PostProcessedWikis` directory contains all wikis in their final postprocessed form. After successfully running the program, the user should find the final output here. 
- The `statistics` directory contains various statistics files that are created throughout the process.
