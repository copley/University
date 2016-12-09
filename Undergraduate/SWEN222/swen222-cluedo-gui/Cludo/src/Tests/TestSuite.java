package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.controller.ControllerTestSuite;
import tests.model.ModelTestSuite;

/**
 * Created by liam on 5/08/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ModelTestSuite.class,
    ControllerTestSuite.class
})
public class TestSuite {
}

