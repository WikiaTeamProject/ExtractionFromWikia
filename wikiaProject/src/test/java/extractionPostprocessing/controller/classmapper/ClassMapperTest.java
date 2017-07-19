package extractionPostprocessing.controller.classmapper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by D060249 on 19.07.2017.
 */
public class ClassMapperTest {
    @Test
    public void performClassTransformation() throws Exception {

        // dummy implementation of abstract class
        class TestClassMapper extends ClassMapper {

            @Override
            public String mapSingleClass(String classToMap) {
                return null;
            }
        }

        TestClassMapper testMapper = new TestClassMapper();

        // input: perfect class -> nothing should happen
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.performClassTransformation("<http://uni-mannheim.de/HarryPotter/class/Creature>"));

        // input: letter after "/class/" should be capitalized
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.performClassTransformation("<http://uni-mannheim.de/HarryPotter/class/creature>"));

        // input: letter after "/class/" should be capitalized, namespace should be replaced
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.performClassTransformation("<http://dbpedia.org/HarryPotter/class/creature>"));

        // input: letter after "/class/" should be capitalized, namespace should be replaced, Template should be removed
        assertEquals("<http://uni-mannheim.de/HarryPotter/class/Creature>",
                testMapper.performClassTransformation("<http://dbpedia.org/HarryPotter/resource/Template:creature>"));

    }

}