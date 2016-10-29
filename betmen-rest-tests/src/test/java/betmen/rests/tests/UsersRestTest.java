package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.dto.edit.UserEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.UserRegData;
import betmen.rests.common.routes.UserRoutes;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserEndPointsHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UsersRestTest {

    @Test
    public void shouldHaveAccessToOwnData() {
        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin();

        UserEditDTO editData = UserEndPointsHandler.getUserEditData(loggedUser.getUserId());

        assertThat(editData.getUserId(), is(loggedUser.getUserId()));
        assertThat(editData.getUserName(), is(loggedUser.getUserName()));
    }

    @Test
    public void shouldReturnCurrentUserData() {
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();
        UserEditDTO editDTO = UserEndPointsHandler.getCurrentUserEditData();
        assertThat(editDTO.getUserId(), is(user.getUserId()));
        assertThat(editDTO.getUserName(), is(user.getUserName()));
    }

    @Test
    public void shouldUpdateUserData() {
        UserRegData userRegData = RandomUtils.randomUser();
        String oldUserName = userRegData.getName();
        String oldUserLogin = userRegData.getLogin();

        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin(userRegData);

        String newUserName = RandomUtils.generate();
        String newUserLogin = RandomUtils.generate();

        UserEditDTO dto = new UserEditDTO();
        dto.setUserId(loggedUser.getUserId());
        dto.setUserName(newUserName);
        dto.setLogin(newUserLogin);

        UserEndPointsHandler.updateUserData(dto);

        UserEditDTO loadedUserData = UserEndPointsHandler.getCurrentUserEditData();
        assertThat(loadedUserData.getUserId(), is(loggedUser.getUserId()));
        assertThat(loadedUserData.getUserName(), is(newUserName));
        assertThat(loadedUserData.getLogin(), is(oldUserLogin));
    }

    @Test
    public void shouldCheckIfUserAdmin() {
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();
        boolean userIsAdmin = UserEndPointsHandler.isUserAdmin(user.getUserId());
        assertThat(userIsAdmin, is(Boolean.FALSE));
    }

    @Test
    public void shouldNotUpdateAnotherUserData() {
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUser();

        // register new user
        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin();

        String newUserName = RandomUtils.generate();
        String newUserLogin = RandomUtils.generate();

        UserEditDTO dto = new UserEditDTO();
        dto.setUserId(anotherUser.getUserId());
        dto.setUserName(newUserName);
        dto.setLogin(newUserLogin);

        RequestHelper.doJsonPost(UserRoutes.USER_UPDATE, dto, UserEndPointsHandler.getParams(dto.getUserId()), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotUpdateWithEmptyData() {
        // register new user
        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin();

        UserEditDTO dto = new UserEditDTO();
        dto.setUserId(loggedUser.getUserId());
        dto.setUserName(StringUtils.EMPTY);
        dto.setLogin(StringUtils.EMPTY);

        RequestHelper.doJsonPost(UserRoutes.USER_UPDATE, dto, UserEndPointsHandler.getParams(dto.getUserId()), HttpStatus.SC_BAD_REQUEST);
    }
}
