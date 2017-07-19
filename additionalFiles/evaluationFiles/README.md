These are the files are used as a gold standard to evaluate mappers. They are not required to run the program but can be used to evaluate new (self-created) mappers. 

The gold-standard is using DBpedia as of 2016-10.
If you want to contribute, download the corresponding files from <a href="http://wiki.dbpedia.org/downloads-2016-10">here</a>.

On windows, you can search through a file using `findstr` in the command line.
Example:
```
findstr location dbpedia_2016-10.nt
```
→ This will look for occurrences of "location" in file `dbpedia_2016-10.nt` and print the resulting lines to the console.

## Mapping Resources
Resources are mapped to DBpedia resources.

## Mapping Properties
Properties are mapped to DBpedia ontologies, if they are not available to DBpedia properties.

## Mapping Classes
Classes are derived from templates and are mapped to DBpedia ontologies.
