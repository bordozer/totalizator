package betmen.rests.tests;

import org.testng.annotations.BeforeClass;

public abstract class AbstractRestTest {

    @BeforeClass
    public void beforeClass() {
        beforeTest();
    }

    protected void beforeTest() {

    }
}
