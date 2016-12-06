package betmen.rests.stories;

import betmen.rests.utils.helpers.AuthEndPointsHandler;
import org.jbehave.core.annotations.When;

public class AbstractCommonStepsStory extends AbstractStory {

    @When("Admin logged in")
    public void iLoginAsAdmin() {
        AuthEndPointsHandler.loginAsAdmin();
    }
}
