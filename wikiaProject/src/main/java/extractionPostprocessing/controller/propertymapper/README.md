If you want to implement a class mapper, write a class that extends 
`PropertyMapper` and implement the following method:
````
public abstract String mapSingleProperty(String propertyToMap);
````
The `MappingExecutor` calls the implemented method. A sample for 
`propertyToMap` would be: `<http://dbpedia.org/property/type>` 
(note that the domain has not been changed yet).<br/><br/> 
To make the program use your mapper, set it in the constructor 
`MappingExecutor` in class `ExtractionPostprocessingApplication`.
