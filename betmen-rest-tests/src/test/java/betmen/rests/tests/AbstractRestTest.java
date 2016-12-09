package betmen.rests.tests;

import betmen.rests.utils.data.DataCleanUpUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractRestTest {

    @BeforeClass
    public void beforeClass() {
        beforeTest();
    }

    @BeforeMethod
    public void beforeEachMethod() {
    }

    @AfterMethod
    public void afterEachMethod() {
    }

    protected void beforeTest() {

    }
}
