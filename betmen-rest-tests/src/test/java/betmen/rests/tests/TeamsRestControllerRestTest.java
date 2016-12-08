package betmen.rests.tests;

import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.TeamEndPointHandler;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class TeamsRestControllerRestTest {

    private static final int ID_OF_NOTHING = 45566;
    private PointsCalculationStrategyEditDTO strategy;

    @BeforeClass
    public void sauteInit() {
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();
        strategy = AdminTestDataGenerator.createPointsStrategy();
    }

    @Test
    public void shouldReturnUnprocessableEntityIfTeamIdDoesNotExist() {
        AuthEndPointsHandler.loginAsAdmin();
        TeamEndPointHandler.getTeam(ID_OF_NOTHING, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldFailGettingNotExistingCategoryTeams() {
        TeamEndPointHandler.getAllCategoryTeams(ID_OF_NOTHING, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetTeam() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        // when
        TeamDTO team = TeamEndPointHandler.getTeam(team1.getTeamId());

        // then
        ComparisonUtils.assertTheSame(team, team1);
    }

    @Test
    public void shouldGetAllCategory1Team() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        // when
        List<TeamDTO> teams = TeamEndPointHandler.getAllCategoryTeams(category.getCategoryId());

        // then
        assertThat(teams, hasSize(2));
        ComparisonUtils.assertTheSame(teams.get(0), team1);
        ComparisonUtils.assertTheSame(teams.get(1), team2);
    }

    @Test
    public void shouldGetAllCategory2Team() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        TeamEditDTO team3 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        // when
        List<TeamDTO> teams = TeamEndPointHandler.getAllCategoryTeams(category.getCategoryId());

        // then
        assertThat(teams, hasSize(1));
        ComparisonUtils.assertTheSame(teams.get(0), team3);
    }

    @Test
    public void shouldGetAllCategory3Team() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();

        // when
        List<TeamDTO> teams = TeamEndPointHandler.getAllCategoryTeams(category.getCategoryId());

        // then
        assertThat(teams, notNullValue());
        assertThat(teams, hasSize(0));
    }

    @Test
    public void shouldFailTeamsGettingNotExistingCup() {
        TeamEndPointHandler.getAllCupTeams(ID_OF_NOTHING, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldFailActiveTeamsGettingNotExistingCup() {
        TeamEndPointHandler.getAllCupActiveTeams(ID_OF_NOTHING, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetAllCupTeam() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), strategy.getPcsId());
        TeamEditDTO inactiveTeam = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO activeTeam = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), 1);

        // when
        List<TeamDTO> teams = TeamEndPointHandler.getAllCupTeams(cup.getCupId());

        // then
        assertThat(teams, hasSize(2));
        ComparisonUtils.assertTheSame(teams.get(0), inactiveTeam);
        ComparisonUtils.assertTheSame(teams.get(1), activeTeam);
    }

    @Test
    public void shouldGetAllCupActiveTeam() {
        // given
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), strategy.getPcsId());
        TeamEditDTO inactiveTeam = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO activeTeam = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());

        // when
        List<TeamDTO> teams = TeamEndPointHandler.getAllCupActiveTeams(cup.getCupId());

        // then
        assertThat(teams, hasSize(1));
        ComparisonUtils.assertTheSame(teams.get(0), activeTeam);
    }
}
