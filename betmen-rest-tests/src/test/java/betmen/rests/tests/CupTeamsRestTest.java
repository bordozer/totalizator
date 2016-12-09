package betmen.rests.tests;

import betmen.dto.dto.CupTeamsDTO;
import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.CupTeamsEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminTeamEndPointsHandler;
import com.google.common.collect.Lists;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class CupTeamsRestTest {

    private TeamEditDTO team1;
    private TeamEditDTO team2;
    private TeamEditDTO team3;
    private TeamEditDTO team4;
    private TeamEditDTO team5;
    private TeamEditDTO team6;

    private CupEditDTO cup1;
    private CupEditDTO cup2;
    private CupEditDTO cup3;

    @BeforeTest
    public void testsInit() {
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO category1 = AdminTestDataGenerator.createCategory();
        CategoryEditDTO category2 = AdminTestDataGenerator.createCategory();
        CategoryEditDTO category3 = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        team1 = createTeam(category1, "Bulls");
        team2 = createTeam(category2, "Golden State");
        team3 = createTeam(category1, "Oklahoma");
        team4 = createTeam(category2, "Atlanta");
        team5 = createTeam(category1, "Brooklyn");
        team6 = createTeam(category3, "Orlando");

        CupEditDTO cupEdit1 = CupTemplater.random(category1.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 2, 0, 0))
                .build();
        cup1 = AdminCupEndPointsHandler.create(cupEdit1);

        CupEditDTO cupEdit2 = CupTemplater.random(category1.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 4, 0, 0))
                .build();
        cup2 = AdminCupEndPointsHandler.create(cupEdit2);

        CupEditDTO cupEdit3 = CupTemplater.random(category2.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 1, 0, 0))
                .build();
        cup3 = AdminCupEndPointsHandler.create(cupEdit3);

    }

    @BeforeMethod
    public void testInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessToCupTeamsForAnonymousUser() {
        CupTeamsEndPointsHandler.getCupTeams(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnCupTeamsWithoutAnyFilters() {
        // given
        AuthEndPointsHandler.loginAsAdmin();

        // then CUP 1
        // when
        CupTeamsDTO cup1Teams = CupTeamsEndPointsHandler.getCupTeams(cup1.getCupId());

        // then
        assertThat(cup1Teams, notNullValue());

        List<String> letters1 = Lists.newArrayList(cup1Teams.getLetters());
        assertThat(letters1, hasSize(2));
        assertThat(letters1, containsInAnyOrder("B", "O"));

        List<TeamDTO> teams1 = cup1Teams.getTeams();
        assertThat(teams1, hasSize(3));
        ComparisonUtils.assertTheSame(teams1.get(0), team5);
        ComparisonUtils.assertTheSame(teams1.get(1), team1);
        ComparisonUtils.assertTheSame(teams1.get(2), team3);

        // then CUP 2
        // when
        CupTeamsDTO cup2Teams = CupTeamsEndPointsHandler.getCupTeams(cup2.getCupId());

        // then
        assertThat(cup2Teams, notNullValue());

        List<String> letters2 = Lists.newArrayList(cup2Teams.getLetters());
        assertThat(letters2, hasSize(2));
        assertThat(letters2, containsInAnyOrder("B", "O"));

        List<TeamDTO> teams2 = cup2Teams.getTeams();
        assertThat(teams2, hasSize(3));
        ComparisonUtils.assertTheSame(teams2.get(0), team5);
        ComparisonUtils.assertTheSame(teams2.get(1), team1);
        ComparisonUtils.assertTheSame(teams2.get(2), team3);

        // then CUP 3
        // when
        CupTeamsDTO cup3Teams = CupTeamsEndPointsHandler.getCupTeams(cup3.getCupId());

        // then
        assertThat(cup3Teams, notNullValue());

        List<String> letters3 = Lists.newArrayList(cup3Teams.getLetters());
        assertThat(letters3, hasSize(2));
        assertThat(letters3, containsInAnyOrder("A", "G"));

        List<TeamDTO> teams3 = cup3Teams.getTeams();
        assertThat(teams3, hasSize(2));
        ComparisonUtils.assertTheSame(teams3.get(0), team4);
        ComparisonUtils.assertTheSame(teams3.get(1), team2);
    }

    @Test
    public void shouldReturnCupTeamsFilteredByFirstLetter() {
        // given
        AuthEndPointsHandler.loginAsAdmin();

        // then CUP 1
        // when
        CupTeamsDTO cup1Teams = CupTeamsEndPointsHandler.getCupByFirstLetter(cup1.getCupId(), "B");

        // then
        assertThat(cup1Teams, notNullValue());

        List<String> letters1 = Lists.newArrayList(cup1Teams.getLetters());
        assertThat(letters1, hasSize(2));
        assertThat(letters1, containsInAnyOrder("B", "O"));

        List<TeamDTO> teams1 = cup1Teams.getTeams();
        assertThat(teams1, hasSize(2));
        ComparisonUtils.assertTheSame(teams1.get(0), team5);
        ComparisonUtils.assertTheSame(teams1.get(1), team1);

        // then CUP 2
        // when
        CupTeamsDTO cup2Teams = CupTeamsEndPointsHandler.getCupByFirstLetter(cup2.getCupId(), "o");

        // then
        assertThat(cup2Teams, notNullValue());

        List<String> letters2 = Lists.newArrayList(cup2Teams.getLetters());
        assertThat(letters2, hasSize(2));
        assertThat(letters2, containsInAnyOrder("B", "O"));

        List<TeamDTO> teams2 = cup2Teams.getTeams();
        assertThat(teams2, hasSize(1));
        ComparisonUtils.assertTheSame(teams2.get(0), team3);

        // then CUP 3
        // when
        CupTeamsDTO cup3Teams = CupTeamsEndPointsHandler.getCupByFirstLetter(cup3.getCupId(), "D");

        // then
        assertThat(cup3Teams, notNullValue());

        List<String> letters3 = Lists.newArrayList(cup3Teams.getLetters());
        assertThat(letters3, hasSize(2));
        assertThat(letters3, containsInAnyOrder("A", "G"));

        List<TeamDTO> teams3 = cup3Teams.getTeams();
        assertThat(teams3, notNullValue());
        assertThat(teams3, hasSize(0));
    }

    // TODO: test active cup's teams loading. Currently the functionality is broken

    private TeamEditDTO createTeam(final CategoryEditDTO category, final String teamName) {
        return AdminTeamEndPointsHandler.create(new TeamEditDtoBuilder()
                .withCategory(category.getCategoryId())
                .withName(teamName)
                .build()
        );
    }
}
