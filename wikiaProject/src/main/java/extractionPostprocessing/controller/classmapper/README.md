If you want to implement a class mapper, write a class that extends 
`ClassMapper` and implement the following method:
````
public abstract String mapSingleClass(String classToMap);
````
The `MappingExecutor` calls the implemented method. A sample for 
`classToMap` would be: 
`<http://dbpedia.org/resource/Template:Person_Infobox>` <br/> <br/> 
Note that the class has not been changed yet and is still realized
as a template. You can easily change that to a class by calling
method `transformTemplateToOntology` which is implemented in 
abstract class `ClassMapper` from which you are inheriting
(so the method can be easily called using 
`this.transformTemplateToOntology`).<br/><br/> 
To make the program use your mapper, set it in the constructor 
`MappingExecutor` in class `ExtractionPostprocessingApplication`.
