package betmen.rests.tests;

import betmen.rests.utils.data.DataCleanUpUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;

@Slf4j
public abstract class AbstractCleanableRestTest extends AbstractRestTest {

    @BeforeClass
    @Override
    public void beforeClass() {
        DataCleanUpUtils.cleanupAll();
        super.beforeClass();
    }
}
