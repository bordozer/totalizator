package betmen.rests.stories;

import betmen.rests.utils.helpers.AuthEndPointsHandler;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;

public class AbstractCommonStepsStory extends AbstractStory {

    @When("$userName logged in")
    public void iLoginAsAdmin(@Named("userName") final String userName) {
        AuthEndPointsHandler.loginAsAdmin();
    }
}
