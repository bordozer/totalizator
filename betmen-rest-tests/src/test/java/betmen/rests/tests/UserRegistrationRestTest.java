package betmen.rests.tests;

import betmen.dto.dto.NewUserDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserRegResponse;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.UserRegData;
import betmen.rests.common.routes.UserRoutes;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserRegistrationRestTest {

    public static final String FIELD_LOGIN = "login";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PASSWORD = "password";

    @Test
    public void shouldNotRegisterUserIfNoDataProvided() {
        Response response = RequestHelper.doJsonPut(UserRoutes.USER_CREATE, new NewUserDTO("", "", ""), HttpStatus.SC_BAD_REQUEST);

        FieldErrorsResponse registrationResponse = response.as(FieldErrorsResponse.class);

        assertThat(6, is(registrationResponse.errorsCount()));

        assertThat(registrationResponse.getFieldErrorResource(FIELD_LOGIN, "errors.login_must_not_be_empty"), notNullValue());
        assertThat(registrationResponse.getFieldErrorResource(FIELD_LOGIN, "errors.login_has_incorrect_length"), notNullValue());

        assertThat(registrationResponse.getFieldErrorResource(FIELD_NAME, "errors.username_must_not_be_empty"), notNullValue());
        assertThat(registrationResponse.containsError(FIELD_NAME, "errors.name_has_incorrect_length"), is(true));

        assertThat(registrationResponse.getFieldErrorResource(FIELD_PASSWORD, "errors.password_must_not_be_empty"), notNullValue());
        assertThat(registrationResponse.getFieldErrorResource(FIELD_PASSWORD, "errors.password_has_incorrect_length"), notNullValue());
    }

    @Test
    public void shouldRegisterUser() {
        UserRegData userRegData = RandomUtils.randomUser();
        Response response = AuthEndPointsHandler.register(userRegData);

        UserRegResponse userRegResponse = response.as(UserRegResponse.class);
        assertThat(userRegResponse.isSuccess(), is(true));
        assertThat(userRegResponse.getErrors().size(), is(0));

        UserDTO user = userRegResponse.getUser();
        assertThat(user.getUserId(), notNullValue());
        assertThat(user.getUserId() > 0, is(true));
        assertThat(userRegData.getName(), is(user.getUserName()));
    }
}
