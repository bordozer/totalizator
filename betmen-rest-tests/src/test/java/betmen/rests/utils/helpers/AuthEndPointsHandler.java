package betmen.rests.utils.helpers;

import betmen.dto.dto.AuthResponse;
import betmen.dto.dto.NewUserDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserRegResponse;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.RestTestUser;
import betmen.rests.common.SecurityCookieFilter;
import betmen.rests.common.UserRegData;
import betmen.rests.common.routes.AuthRoutes;
import betmen.rests.common.routes.UserRoutes;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.Map;

@Slf4j
public class AuthEndPointsHandler {

    public static UserDTO registerNewUserAndLogin() {
        return registerNewUserAndLogin(RandomUtils.randomUser());
    }

    public static UserDTO registerNewUserAndLogin(final UserRegData userData) {
        UserDTO userDTO = registerNewUser(userData);
        login(userData.getLogin(), userData.getPassword());
        return userDTO;
    }

    public static UserDTO registerNewUser() {
        return registerNewUser(RandomUtils.randomUser());
    }

    public static UserDTO registerNewUser(final UserRegData userData) {
        LOGGER.debug(String.format("Register new user: '%s' (name: %s), password '%s'", userData.getLogin(), userData.getName(), userData.getPassword()));
        return register(userData.getLogin(), userData.getName(), userData.getPassword()).as(UserRegResponse.class).getUser();
    }

    public static AuthResponse login(final String login, final String password) {
        LOGGER.debug(String.format("Login as '%s', password '%s'", login, password));
        return login(login, password, HttpStatus.SC_OK).as(AuthResponse.class);
    }

    public static AuthResponse login(final UserRegData userData) {
        LOGGER.debug(String.format("Login as '%s' (name: %s), password '%s'", userData.getLogin(), userData.getName(), userData.getPassword()));
        return login(userData.getLogin(), userData.getPassword());
    }

    public static RestTestUser loginAsAdmin() {
        return new RestTestUser(readUserId(login(RestTestConstants.ADMIN_LOGIN, RestTestConstants.ADMIN_PASS, HttpStatus.SC_OK).as(AuthResponse.class)));
    }

    public static AuthResponse logout() {
        return RequestHelper.doGet(AuthRoutes.LOGOUT).as(AuthResponse.class);
    }

    public static int readUserId(AuthResponse response) {
        return Integer.parseInt(response.getDetails().get(AuthResponse.USER_ID));
    }

    public static Response register(final UserRegData userRegData) {
        return register(userRegData.getLogin(), userRegData.getName(), userRegData.getPassword());
    }

    public static Response register(final String login, final String userName, final String password) {
        startSession();
        return RequestHelper.doJsonPut(UserRoutes.USER_CREATE, new NewUserDTO(login, userName, password), HttpStatus.SC_OK);
    }

    public static Response login(final String login, final String password, final int expectedStatusCode) {
        startSession();
        Map<String, Object> params = new HashedMap<>();
        params.put("login", login);
        params.put("password", password);
        return RequestHelper.doJsonPost(AuthRoutes.LOGIN, StringUtils.EMPTY, params, expectedStatusCode);
    }

    private static void startSession() {
        RestAssured.reset();
        SecurityCookieFilter securityCookieFilter = new SecurityCookieFilter();
        RestAssured.filters(securityCookieFilter);
    }
}
