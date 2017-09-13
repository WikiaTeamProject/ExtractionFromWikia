import applications.extractionPostprocessing.controller.DBpediaResourceServiceOfflineTest;
import applications.extractionPostprocessing.controller.DBpediaResourceServiceOnlineTest;
import applications.extractionPostprocessing.controller.MappingEvaluatorTest;
import applications.extractionPostprocessing.controller.RedirectProcessorSingleWikiTest;
import applications.extractionPostprocessing.controller.classmapper.ClassMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testOrchestration.CheckPrerequisitesTest;
import utils.IOoperationsTest;

/**
 * This suite should contain all tests and is here just for convenience.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ClassMapperTest.class,
                DBpediaResourceServiceOfflineTest.class,
                DBpediaResourceServiceOnlineTest.class,
                MappingEvaluatorTest.class,
                RedirectProcessorSingleWikiTest.class,
                CheckPrerequisitesTest.class,
                IOoperationsTest.class
        }

)

public class AllTestsTestSuite {
}
