package extractionPostprocessing.controller;

import extractionPostprocessing.model.ClassMapper;

/**
 * Trivial implementation of a class mapper.
 * - maps all classes to dbpedia classes (same name)
 */
public class ClassMapper_1 extends ClassMapper{

    @Override
    public String mapSingleClass(String classToMap) {
        return classToMap;
    }
}
