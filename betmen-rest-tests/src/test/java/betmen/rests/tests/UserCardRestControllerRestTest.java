package betmen.rests.tests;

import betmen.dto.dto.UserCardDTO;
import betmen.dto.dto.UserDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserCardEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class UserCardRestControllerRestTest {

    @BeforeClass
    public void initClass() {
        DataCleanUpUtils.cleanupAll();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldReturn404ForAnonymousUser() {
        UserCardEndPointsHandler.getUserCardOnDate(0, LocalDate.now(), ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnprocessableEntityIfUserDoesNotExist() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserCardEndPointsHandler.getUserCardOnDate(548885, LocalDate.now(), ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldReturnBadRequestIfDateIsNotProvided() {
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();
        UserCardEndPointsHandler.getUserCardOnDate(user.getUserId(), null, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldGetOwnCardData() {
        // given
        UserDTO userDTO = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        UserCardDTO userCardData = UserCardEndPointsHandler.getUserCardOnDate(userDTO.getUserId(), LocalDate.now());

        //then
        assertThat(userCardData, notNullValue());
        assertThat(userCardData.getCupsToShow(), notNullValue());
        assertThat(userCardData.getCupsToShow(), hasSize(0));
    }

    @Test
    public void shouldGetAnotherUserCardData() {
        // given
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUser();
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        UserCardDTO userCardData = UserCardEndPointsHandler.getUserCardOnDate(anotherUser.getUserId(), LocalDate.now());

        //then
        assertThat(userCardData, notNullValue());
        assertThat(userCardData.getCupsToShow(), notNullValue());
        assertThat(userCardData.getCupsToShow(), hasSize(0));
    }
}
