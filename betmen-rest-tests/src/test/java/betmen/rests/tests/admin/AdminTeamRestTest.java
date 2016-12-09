package betmen.rests.tests.admin;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminTeamEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdminTeamRestTest {

    private CupEditDTO cup;

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
        cup = AdminTestDataGenerator.createRandomCup();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetTeams() {
        final List<TeamEditDTO> cups = getItems(cup.getCategoryId());
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(0));
        // TODO: check ordering
    }

    @Test
    public void shouldCreateTeam() {
        TeamEditDTO dto = new TeamEditDTO();
        dto.setCategoryId(cup.getCategoryId());
        dto.setTeamName(RandomUtils.teamName());
        dto.setTeamImportId("IMPORT_ID");
        TeamEditDTO created = AdminTeamEndPointsHandler.create(cup.getCupId(), dto);
        assertThat(created, notNullValue());
        assertThat(created.getTeamId() > 0, is(true));
        assertThat(created.getTeamName(), is(dto.getTeamName()));
        assertThat(created.getTeamImportId(), is(dto.getTeamImportId()));
    }

    @Test
    public void shouldGetItemIfExists() {
        TeamEditDTO dto = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        TeamEditDTO team = AdminTeamEndPointsHandler.get(dto.getTeamId());
        assertThat(team, notNullValue());
        assertThat(team.getTeamId(), is(dto.getTeamId()));
        assertThat(team.getTeamName(), is(dto.getTeamName()));
        assertThat(team.getTeamImportId(), is(dto.getTeamImportId()));
    }

    @Test
    public void shouldThrowExceptionIfAttemptToGetNotExistingItem() {
        AdminTeamEndPointsHandler.get(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfNameIsEmpty() {
        assertNotSaved(StringUtils.EMPTY, cup.getCupId(), cup.getCategoryId());
    }

    @Test
    public void shouldNotCreateIfNameIsBlank() {
        assertNotSaved(" ", cup.getCupId(), cup.getCategoryId());
    }

    @Test
    public void shouldCreateIfNameAlreadyExists() {
        TeamEditDTO item = createTeam();
        TeamEditDTO anotherItem = TeamEditDtoBuilder.construct(item.getTeamName(), cup.getCategoryId());
        TeamEditDTO team = AdminTeamEndPointsHandler.create(cup.getCupId(), anotherItem);
        assertThat(team, notNullValue());
        assertThat(team.getTeamName(), is(anotherItem.getTeamName()));
        assertThat(team.getTeamImportId(), is(anotherItem.getTeamImportId()));
        assertThat(team.getCategoryId(), is(anotherItem.getCategoryId()));
    }

    @Test
    public void shouldUpdateIfNameAlreadyExists() {
        CategoryEditDTO anotherCategory = AdminTestDataGenerator.createCategory();
        TeamEditDTO item = createTeam();

        TeamEditDTO anotherItem = createTeam();
        anotherItem.setTeamName(item.getTeamName());
        anotherItem.setTeamImportId("NEW_IMPORT_ID");
        anotherItem.setCategoryId(anotherCategory.getCategoryId());

        TeamEditDTO team = AdminTeamEndPointsHandler.update(anotherItem);

        assertThat(team, notNullValue());
        assertThat(team.getTeamName(), is(anotherItem.getTeamName()));
        assertThat(team.getTeamImportId(), is("NEW_IMPORT_ID"));
        assertThat(team.getCategoryId(), is(anotherCategory.getCategoryId()));
    }

    @Test
    public void shouldNotCreateIfCategoryIsWrong() {
        TeamEditDTO dto = construct();
        dto.setCategoryId(99999);
        AdminTeamEndPointsHandler.create(cup.getCupId(), dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotUpdateIfCategoryIsWrong() {
        TeamEditDTO dto = createTeam();
        dto.setCategoryId(99999);
        AdminTeamEndPointsHandler.update(dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDelete() {
        AdminTeamEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldReturnTrueIfTeamIdIsZero() {
        assertThat(AdminTeamEndPointsHandler.delete(0), is(true));
    }

    @Test
    public void shouldNotDeleteIfWrongEntityId() {
        TeamEditDTO dto = createTeam();
        assertThat(AdminTeamEndPointsHandler.delete(dto.getTeamId()), is(true));
    }

    @Test(enabled = false) // TODO: Disabled
    public void shouldNotDeleteIfTeamAssignedToMatch() {
//        AdminTeamEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(enabled = false) // TODO: disabled because getting cup activity is broken
    public void shouldActivateTeamInCup() {
        // given
        TeamEditDTO team = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        TeamEditDTO teamEdit1 = AdminTeamEndPointsHandler.get(team.getTeamId());
        assertThat(teamEdit1.isTeamChecked(), is(false));

        // when
        AdminTeamEndPointsHandler.activateTeamInCup(cup.getCupId(), team.getTeamId());
        TeamEditDTO teamEdit2 = AdminTeamEndPointsHandler.get(team.getTeamId());
        assertThat(teamEdit2.isTeamChecked(), is(true));

        // when
        AdminTeamEndPointsHandler.deactivateTeamInCup(cup.getCupId(), team.getTeamId());
        TeamEditDTO teamEdit3 = AdminTeamEndPointsHandler.get(team.getTeamId());
        assertThat(teamEdit3.isTeamChecked(), is(false));
    }

    private void assertNotSaved(final String name, final int cupId, final int categoryId) {
        int sizeBefore = getItems(categoryId).size();

        TeamEditDTO dto = new TeamEditDTO();
        dto.setTeamName(name);

        Response response = AdminTeamEndPointsHandler.create(cupId, dto, HttpServletResponse.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("teamName", "errors.name_should_not_be_blank"), Matchers.is(Boolean.TRUE));
        assertThat(errors.containsError("teamName", "errors.name_has_wrong_length"), Matchers.is(Boolean.TRUE));
        assertThat(errors.containsError("categoryId", "must be greater than or equal to 1"), Matchers.is(Boolean.TRUE));

        assertThat(getItems(categoryId).size(), Matchers.is(sizeBefore));
    }

    private List<TeamEditDTO> getItems(final int categoryId) {
        return AdminTeamEndPointsHandler.getTeamOfCategory(categoryId);
    }

    private TeamEditDTO createTeam() {
        return AdminTeamEndPointsHandler.create(cup.getCupId(), TeamEditDtoBuilder.construct(RandomUtils.teamName(), cup.getCategoryId()));
    }

    private TeamEditDTO construct() {
        return TeamEditDtoBuilder.construct(cup.getCategoryId());
    }
}
