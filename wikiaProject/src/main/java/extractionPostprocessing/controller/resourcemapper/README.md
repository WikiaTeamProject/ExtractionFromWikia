If you want to implement a class mapper, write a class that extends 
`ResourceMapper` and implement the following method:
````
public abstract String mapSingleResource(String resourceToMap);
````
The `MappingExecutor` calls the implemented method. A sample for 
`resourceToMap` would be: `<http://dbpedia.org/resource/Stomp>` 
(note that the domain has not been changed yet).<br/><br/> 
To make the program use your mapper, set it in the constructor 
`MappingExecutor` in class `ExtractionPostprocessingApplication`.
