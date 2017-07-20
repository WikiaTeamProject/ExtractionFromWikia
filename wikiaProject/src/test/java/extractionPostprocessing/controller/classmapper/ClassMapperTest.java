package extractionPostprocessing.controller.classmapper;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test implemented method of abstract class {@link extractionPostprocessing.controller.classmapper.ClassMapper ClassMapper}.
 */
public class ClassMapperTest {

    // dummy implementation of abstract class
    static class TestClassMapper extends ClassMapper {

        @Override
        public String mapSingleClass(String classToMap) {
            return null;
        }
    }

    private static TestClassMapper testMapper;

    @BeforeClass
    public static void setUp(){
        testMapper = new TestClassMapper();
    }


    @Test
    public void transformTemplateToOntology() throws Exception {

        // transformation to lowercase
        assertEquals("<http://uni-mannheim.de/HarryPotter/ontology/creature>",
                testMapper.transformTemplateToOntology("<http://uni-mannheim.de/HarryPotter/resource/Template:Creature_infobox>", false));

        // transformation to uppercase
        assertEquals("<http://uni-mannheim.de/HarryPotter/ontology/Creature>",
                testMapper.transformTemplateToOntology("<http://uni-mannheim.de/HarryPotter/resource/Template:Creature_infobox>", true));
    }

    @Test
    public void performClassTransformation() throws Exception {

        // input: perfect class -> nothing should happen
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.transformTemplateToClass("<http://uni-mannheim.de/HarryPotter/class/Creature>"));

        // input: letter after "/class/" should be capitalized
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.transformTemplateToClass("<http://uni-mannheim.de/HarryPotter/class/creature>"));

        // input: letter after "/class/" should be capitalized, namespace should be replaced
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.transformTemplateToClass("<http://dbpedia.org/HarryPotter/class/creature>"));

        // input: letter after "/class/" should be capitalized, namespace should be replaced, Template should be removed
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.transformTemplateToClass("<http://dbpedia.org/HarryPotter/resource/Template:creature>"));

    }

}