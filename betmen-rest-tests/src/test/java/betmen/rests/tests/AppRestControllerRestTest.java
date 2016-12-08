package betmen.rests.tests;

import betmen.dto.dto.AppDTO;
import betmen.dto.dto.UserDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.CommonEndPointHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class AppRestControllerRestTest {

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldReturnIsAuthenticatedForAnonymous() {
        assertThat(CommonEndPointHandler.isAuthenticated(), is(false));
    }

    @Test
    public void shouldReturnIsAuthenticatedForLoggedUser() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        assertThat(CommonEndPointHandler.isAuthenticated(), is(true));
    }

    @Test
    public void shouldReturnWhomIForAnonymous() {
        // given

        // when
        final UserDTO user = CommonEndPointHandler.whoAmI();

        // then
        assertThat(user, notNullValue());
        assertThat(user.getUserId(), is(0));
        assertThat(user.getUserName(), is(CommonEndPointHandler.WHO_AM_I_ANONYMOUS));
    }

    @Test
    public void shouldReturnWhomIForLoggedUser() {
        // given
        final UserRegData regData = RandomUtils.randomUser();
        final UserDTO userDTO = AuthEndPointsHandler.registerNewUserAndLogin(regData);

        // when
        final UserDTO user = CommonEndPointHandler.whoAmI();

        // then
        assertThat(user, notNullValue());
        assertThat(user.getUserId(), is(userDTO.getUserId()));
        assertThat(user.getUserName(), is(userDTO.getUserName()));
    }

    @Test
    public void shouldReturnAppContextForAnonymous() {
        // given

        // when
        AppDTO appContext = CommonEndPointHandler.getAppContext();

        // then
        assertThat(appContext, notNullValue());

        assertThat(appContext.getProjectName(), is("Betmen"));
        assertThat(appContext.getServerNow(), notNullValue());
        assertThat(appContext.getCurrentUser(), nullValue());

        assertThat(appContext.getLanguage(), notNullValue());
        assertThat(appContext.getLanguage().getName(), is("English"));
        assertThat(appContext.getLanguage().getCountry(), is("en"));
    }

    @Test
    public void shouldReturnAppContextForLoggedUser() {
        // given
        final UserRegData regData = RandomUtils.randomUser();
        final UserDTO userDTO = AuthEndPointsHandler.registerNewUserAndLogin(regData);

        // when
        AppDTO appContext = CommonEndPointHandler.getAppContext();

        // then
        assertThat(appContext, notNullValue());

        assertThat(appContext.getProjectName(), is("Betmen"));
        assertThat(appContext.getServerNow(), notNullValue());

        assertThat(appContext.getLanguage(), notNullValue());
        assertThat(appContext.getLanguage().getName(), is("English"));
        assertThat(appContext.getLanguage().getCountry(), is("en"));

        ComparisonUtils.assertEqual(userDTO, appContext.getCurrentUser());
    }
}
