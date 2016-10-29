package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.dto.edit.UserEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UserListRestTest {

    @BeforeMethod
    public void logout() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessForAnonymousUser() {
        UserEndPointsHandler.getUserList(ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnListOfUsersForRegisteredUser() {
        // given
        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        List<UserDTO> userList = UserEndPointsHandler.getUserList();

        // then
        assertThat(userList, notNullValue());
        assertThat(userList.size() >= 1, is(true));

        // assert registered user's data
        UserDTO userDTO = find(userList, loggedUser.getUserId());
        assertThat(userDTO, notNullValue());
        assertThat(userDTO.getUserName(), is(loggedUser.getUserName()));

        // update user's data
        String newName = RandomUtils.UUID();
        UserEditDTO dto = new UserEditDTO();
        dto.setUserId(loggedUser.getUserId());
        dto.setUserName(newName);

        UserEndPointsHandler.updateUserData(dto);

        // getMatch user's data again
        List<UserDTO> userList1 = UserEndPointsHandler.getUserList();
        UserDTO userDTO1 = find(userList1, loggedUser.getUserId());

        // assert registered user's data again
        assertThat(userDTO1, notNullValue());
        assertThat(userDTO1.getUserName(), is(newName));
    }

    private UserDTO find(final List<UserDTO> userList, final int userId) {
        return userList.stream().filter(userDto -> userDto.getUserId() == userId).findFirst().orElse(null);
    }
}
