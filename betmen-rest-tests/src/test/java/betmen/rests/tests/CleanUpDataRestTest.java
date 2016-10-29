package betmen.rests.tests;

import betmen.rests.utils.data.DataCleanUpUtils;
import org.testng.annotations.Test;

public class CleanUpDataRestTest {

    @Test
    public void shouldCleanUpAllData() {
        DataCleanUpUtils.cleanupAll();
    }
}
