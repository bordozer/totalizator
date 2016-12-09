package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserListItemDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.edit.UserEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.UserEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.google.common.collect.Lists;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

public class UserListRestTest {

    @BeforeMethod
    public void logout() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessForAnonymousUser() {
        UserEndPointsHandler.getAllUsers(ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnAllUsers() {
        // given
        UserDTO loggedUser = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        List<UserDTO> userList = UserEndPointsHandler.getAllUsers();

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

        UserEndPointsHandler.modifyCurrentUserSettings(dto);

        // getMatch user's data again
        List<UserDTO> userList1 = UserEndPointsHandler.getAllUsers();
        UserDTO userDTO1 = find(userList1, loggedUser.getUserId());

        // assert registered user's data again
        assertThat(userDTO1, notNullValue());
        assertThat(userDTO1.getUserName(), is(newName));
    }

    @Test
    public void shouldReturnListOfUsersForRegisteredUser() {
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team3 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team4 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());

        MatchEditDTO matchEdit1 = MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId()).future(2).build();
        MatchEditDTO match1 = AdminMatchEndPointsHandler.create(matchEdit1);
        MatchEditDTO matchEdit2 = MatchTemplater.random(cup.getCupId(), team3.getTeamId(), team4.getTeamId()).future(3).build();
        MatchEditDTO match2 = AdminMatchEndPointsHandler.create(matchEdit2);

        UserDTO noBetUse = AuthEndPointsHandler.registerNewUserAndLogin();

        UserDTO oneBetUser = AuthEndPointsHandler.registerNewUserAndLogin();
        BetEndPointsHandler.make(match1.getMatchId(), 1, 2);

        UserDTO twoBetsUser = AuthEndPointsHandler.registerNewUserAndLogin();
        BetEndPointsHandler.make(match1.getMatchId(), 3, 4);
        BetEndPointsHandler.make(match2.getMatchId(), 5, 6);

        List<UserDTO> expectedUserList = Lists.newArrayList(noBetUse, oneBetUser, twoBetsUser).stream()
                .sorted((o1, o2) -> o1.getUserName().compareToIgnoreCase(o2.getUserName()))
                .collect(Collectors.toList());

        List<UserListItemDTO> actualUserList = UserEndPointsHandler.getUserList().stream()
                .filter(userListItem -> expectedUserList.stream()
                        .map(UserDTO::getUserId)
                        .collect(Collectors.toList())
                        .contains(userListItem.getUser().getUserId())
                )
                .collect(Collectors.toList());
        assertThat(actualUserList, hasSize(3));

        UserListItemDTO userItem1 = actualUserList.get(0);
        ComparisonUtils.assertEqual(userItem1.getUser(), expectedUserList.get(0));

        UserListItemDTO userItem2 = actualUserList.get(0);
        ComparisonUtils.assertEqual(userItem2.getUser(), expectedUserList.get(1));

        UserListItemDTO userItem3 = actualUserList.get(0);
        ComparisonUtils.assertEqual(userItem3.getUser(), expectedUserList.get(2));

        assertThat(getUser(actualUserList, noBetUse).getBetsCount(), is(0));
        assertThat(getUser(actualUserList, oneBetUser).getBetsCount(), is(1));
        assertThat(getUser(actualUserList, twoBetsUser).getBetsCount(), is(2));
    }

    private UserListItemDTO getUser(final List<UserListItemDTO> actualUserList, final UserDTO userDto) {
        return actualUserList.stream().filter(item -> item.getUser().getUserId() == userDto.getUserId()).findFirst().get();
    }

    private UserDTO find(final List<UserDTO> userList, final int userId) {
        return userList.stream().filter(userDto -> userDto.getUserId() == userId).findFirst().orElse(null);
    }
}
