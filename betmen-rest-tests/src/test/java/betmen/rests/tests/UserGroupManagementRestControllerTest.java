package betmen.rests.tests;

import betmen.dto.dto.UserGroupEditDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserGroupEndPointsHandler;
import com.google.common.collect.Lists;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class UserGroupManagementRestControllerTest {

    private CupEditDTO cup1;
    private CupEditDTO cup2;
    private CupEditDTO cup3;

    @BeforeClass
    public void classInit() {
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        cup1 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        cup2 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        cup3 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
    }

    @BeforeMethod
    public void testInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessForAnonymousUser() {
        UserGroupEndPointsHandler.getAllCurrentUserGroups(ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnEmptyListIfCurrentUserHasNotGroups() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(0));
    }

    @Test
    public void shouldReturnErrorIfGroupNameNotProvided() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserGroupEndPointsHandler.create(new UserGroupEditDTO(), ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCreateUserGroupIfOnlyNameProvided() {
        String userGroupName = "Friends user group";
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName(userGroupName);

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);
        assertThat(created, notNullValue());
        assertThat(created.getUserGroupId(), notNullValue());
        assertThat(created.getUserGroupId() > 0, is(true));
        assertThat(created.getUserGroupName(), is(userGroupName));
        assertThat(created.getCupIds(), hasSize(0));

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(1));
        ComparisonUtils.assertEqual(created, userGroups.get(0));
    }

    @Test
    public void shouldCreateTwoUserGroupWithTheSameName() {
        String userGroupName = "Not unique user group name";
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed1 = new UserGroupEditDTO();
        constructed1.setUserGroupName(userGroupName);

        UserGroupEditDTO constructed2 = new UserGroupEditDTO();
        constructed2.setUserGroupName(userGroupName);

        UserGroupEditDTO created1 = UserGroupEndPointsHandler.create(constructed1);
        UserGroupEditDTO created2 = UserGroupEndPointsHandler.create(constructed2);

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(2));
    }

    @Test
    public void shouldCreateUserGroupWithCups() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Family user group");
        constructed.setCupIds(Lists.newArrayList(cup1.getCupId(), cup3.getCupId()));

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(1));
        ComparisonUtils.assertEqual(created, userGroups.get(0));
    }

    @Test
    public void shouldReturn422IfOneOfProvidedCupsDoesNotExist() {
        int cupIdDoesNotExist = 334;
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Family user group");
        constructed.setCupIds(Lists.newArrayList(cup1.getCupId(), cup3.getCupId(), cupIdDoesNotExist));

        UserGroupEndPointsHandler.create(constructed, ResponseStatus.UNPROCESSABLE_ENTITY);

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(0));
    }

    @Test
    public void shouldUpdateUserGroupWithCups() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Animals user group");
        constructed.setCupIds(Lists.newArrayList(cup1.getCupId(), cup2.getCupId()));

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);
        created.setUserGroupName("Cat's user group");
        created.setCupIds(Lists.newArrayList(cup3.getCupId()));

        UserGroupEditDTO updated = UserGroupEndPointsHandler.update(created);

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(1));
        ComparisonUtils.assertEqual(updated, userGroups.get(0));
    }

    @Test
    public void shouldNotUpdateUserGroupIfNameIsNotProvided() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Pig's user group");

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);
        created.setUserGroupName("");

        UserGroupEndPointsHandler.update(created, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotUpdateUserGroupIfIdIsWrong() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupId(5886); // Group ID does not exist
        constructed.setUserGroupName("Pig's user group");

        UserGroupEndPointsHandler.update(constructed, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotUpdateExistingUserGroupOfAnotherUser() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Enemies user group");

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);

        AuthEndPointsHandler.registerNewUserAndLogin();
        UserGroupEndPointsHandler.update(created, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteUserGroup() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserGroupEditDTO constructed = new UserGroupEditDTO();
        constructed.setUserGroupName("Boys user group");

        UserGroupEditDTO created = UserGroupEndPointsHandler.create(constructed);

        List<UserGroupEditDTO> userGroups = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups, hasSize(1));

        UserGroupEndPointsHandler.delete(created.getUserGroupId());
        List<UserGroupEditDTO> userGroups1 = UserGroupEndPointsHandler.getAllCurrentUserGroups();
        assertThat(userGroups1, hasSize(0));
    }

    @Test
    public void shouldNotDeleteIfUserGroupDoesNotExist() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserGroupEndPointsHandler.delete(4567, ResponseStatus.UNPROCESSABLE_ENTITY);
    }
}
