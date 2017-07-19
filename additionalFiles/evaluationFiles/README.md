These are the files are used as a gold standard to evaluate mappers. They are not required to run the program but can be used to evaluate new (self-created) mappers. 

The gold-standard is using DBpedia as of 2016-10.
If you want to contribute, download the corresponding files from <a href="http://wiki.dbpedia.org/downloads-2016-10">here</a>.

On windows, you can search through a file using `findstr` in the command line:
```
findstr location dbpedia_2016-10.nt
```


## Mapping Resources
Resources are mapped to DBpedia resources.

## Mapping Properties
Properties are mapped to DBpedia ontologies, if they are not available to DBpedia properties.
You can find a good overview of the ontology <a href="http://mappings.dbpedia.org/server/ontology/classes/">here</a>.

## Mapping Classes
Classes are derived from templates and are mapped to DBpedia ontologies.
