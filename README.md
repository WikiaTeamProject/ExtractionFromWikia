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
<TODO: file structure (very quick there is a larger section below), config file, what to download etc.>


## Implementation Details

### File Structure
In the `config.properties` file, you specify a root directory. For a regular program run-through this is already sufficient. If you want to extend the coding or execute only parts of the complete chain of commands, you have to know about the file structure.

```
root_directory
+---downloadedWikis
|     +---downloaded
|         +---7z
|         +---gz
|     +---extracted
|     +---DbPediaExtractionFormat
|     +---PostProcessedWikis
```

The program performs a lot of file operations. All of those file operations are handled within the `root_directory` that you specify.
The `downloadedWiki` directory... <todo>
The `7z` directory contains all wikis that were downloaded in the 7z format.
The `gz` directory contains all wikis that were downloaded the gz format.

