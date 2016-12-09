package betmen.rests.tests;

import betmen.dto.dto.AuthResponse;
import betmen.dto.dto.UserDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserAuthRestTest {

    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String FULL_AUTHENTICATION_IS_REQUIRED_TO_ACCESS_THIS_RESOURCE = "Full authentication is required to access this resource";
    private static final String AUTHENTICATION_FAILURE = "Authentication failure";

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldFailNonPublicResourcesGettingIfUnauthorized() {
        Response response = UserEndPointsHandler.getCurrentUserSettings(ResponseStatus.UNAUTHORIZED);
        AuthResponse authResponse = response.as(AuthResponse.class);

        assertThat(authResponse.getDetails().get(AuthResponse.AUTH_RESULT), is(UNAUTHORIZED));
        assertThat(authResponse.getDetails().get(AuthResponse.ERROR), is(FULL_AUTHENTICATION_IS_REQUIRED_TO_ACCESS_THIS_RESOURCE));
    }

    @Test
    public void shouldFailLoginIfCredentialsAreWrong() {
        UserRegData userData = RandomUtils.randomUser();
        Response response = AuthEndPointsHandler.login(userData.getLogin(), userData.getPassword(), HttpStatus.SC_UNAUTHORIZED);
        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(authResponse.getDetails().get(AuthResponse.AUTH_RESULT), is(AUTHENTICATION_FAILURE));
    }

    @Test
    public void shouldGoThrough() {
        UserRegData anotherUserData = RandomUtils.randomUser();
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUser(anotherUserData);

        UserRegData userData = RandomUtils.randomUser();

        // register new user
        UserDTO registeredUser = AuthEndPointsHandler.registerNewUser(userData);

        // registration of new user is not a authentication - non public resources are not available
        Response userDataResponse2 = UserEndPointsHandler.getCurrentUserSettings(ResponseStatus.UNAUTHORIZED);
        AuthResponse userDataResponse2AuthResponse = userDataResponse2.as(AuthResponse.class);
        assertThat(userDataResponse2AuthResponse.getDetails().get(AuthResponse.AUTH_RESULT), is(UNAUTHORIZED));

        // login as earlie registered user
        AuthResponse authResponse2 = AuthEndPointsHandler.login(userData.getLogin(), userData.getPassword());

        // log out
        AuthResponse logoutAuthResponse = AuthEndPointsHandler.logout();
        assertThat(logoutAuthResponse.getResponseCode(), is(HttpStatus.SC_OK));

        // non public resources are not available again
        Response userDataResponse4 = UserEndPointsHandler.getCurrentUserSettings(ResponseStatus.UNAUTHORIZED);
        AuthResponse taskList4AuthResponse = userDataResponse4.as(AuthResponse.class);
        assertThat(taskList4AuthResponse.getDetails().get(AuthResponse.AUTH_RESULT), is(UNAUTHORIZED));
    }
}
