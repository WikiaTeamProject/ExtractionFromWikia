package extractionPostprocessing.controller.classmapper;

/**
 * THE OUTPUT OF THIS MAPPER IS INCORRECT.
 */
public class ClassMapper_2 extends ClassMapper{

    @Override
    public String mapSingleClass(String classToMap) {

        String resourceToMap=classToMap.toLowerCase();
        String mappedClass="<NULL>";

        if(resourceToMap.contains("/template:")){
            System.out.println(resourceToMap.indexOf("/",resourceToMap.indexOf("/template:")+1));

            if(resourceToMap.indexOf("/",resourceToMap.indexOf("/template:")+1)!=-1){
                String propertyToMap=
                        resourceToMap.substring(0,resourceToMap.indexOf("/template:"))
                                + resourceToMap.substring(
                                resourceToMap.indexOf("/",resourceToMap.indexOf("/template:")+1),
                                resourceToMap.length());


                String ontologyClass=propertyToMap.replace("/resource/","/ontology/");

                System.out.println("Ontology Class : "+ontologyClass);

                mappedClass=ontologyClass;

            }
        }
        return mappedClass;
    }
}
