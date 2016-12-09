package tests.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.model.board.BoardTestSuite;

/**
 * Created by liam on 5/08/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BoardTestSuite.class,
        PlayerTest.class
})
public class ModelTestSuite {
}
