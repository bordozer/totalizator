package betmen.rests.tests;

import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.MatchEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class MatchesRestTest extends AbstractCleanableRestTest {

    private static final int NOT_EXISTING_CUP_ID = -23;

    @BeforeMethod
    public void beforeEachMethod() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessToMatchForAnonymousUser() {
        MatchEndPointsHandler.getMatch(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturn422IfRequestedNotExistingMatch() {
        AuthEndPointsHandler.loginAsAdmin();
        MatchEndPointsHandler.getMatch(0, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetExistingMatch() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        MatchEditDTO matchEdit = AdminTestDataGenerator.createRandomMatch();

        // when
        MatchDTO match = MatchEndPointsHandler.getMatch(matchEdit.getMatchId());

        // then
        assertThat(match, notNullValue());
        ComparisonUtils.assertTheSame(match, matchEdit);
    }

    @Test
    public void shouldReturn422IfCupMatchesAreRequestedAnonymously() {
        MatchEndPointsHandler.getCupMatches(NOT_EXISTING_CUP_ID, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldThrowExceptionIfMatchesOfNotExistingCupAreRequested() {
        AuthEndPointsHandler.loginAsAdmin();
        MatchEndPointsHandler.getCupMatches(NOT_EXISTING_CUP_ID, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldReturnCupMatchesSortedByBeginningTimeDesc() {
        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();

        CupEditDTO cup1 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        CupEditDTO cup2 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        CupEditDTO cup3 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());

        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        MatchEditDTO cup1Match1 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .startingAt(LocalDateTime.of(2016, 5, 21, 19, 0))
                .build());
        MatchEditDTO cup1Match2 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .startingAt(LocalDateTime.of(2016, 5, 20, 19, 0))
                .build());
        MatchEditDTO cup1Match3 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .startingAt(LocalDateTime.of(2016, 5, 22, 20, 0))
                .build());
        MatchEditDTO cup2Match1 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .startingAt(LocalDateTime.of(2016, 5, 23, 19, 0))
                .build());

        List<MatchDTO> cup1Matches = MatchEndPointsHandler.getCupMatches(cup1.getCupId());
        assertThat(cup1Matches, hasSize(3));
        ComparisonUtils.assertTheSame(cup1Matches.get(0), cup1Match3);
        ComparisonUtils.assertTheSame(cup1Matches.get(1), cup1Match1);
        ComparisonUtils.assertTheSame(cup1Matches.get(2), cup1Match2);

        List<MatchDTO> cup2Matches = MatchEndPointsHandler.getCupMatches(cup2.getCupId());
        assertThat(cup2Matches, hasSize(1));
        ComparisonUtils.assertTheSame(cup2Matches.get(0), cup2Match1);

        List<MatchDTO> cup3Matches = MatchEndPointsHandler.getCupMatches(cup3.getCupId());
        assertThat(cup3Matches, notNullValue());
        assertThat(cup3Matches, hasSize(0));
    }

    @Test
    public void shouldReturn422IfCupTeamsMatchesAreRequestedAnonymously() {
        MatchEndPointsHandler.getCupTeamsMatches(NOT_EXISTING_CUP_ID, 1, 2, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnEmptyListIfTeamsMatchesOfNotExistingCupOrTeamsAreRequested() {
        AuthEndPointsHandler.loginAsAdmin();
        List<MatchDTO> cupTeamsMatches = MatchEndPointsHandler.getCupTeamsMatches(NOT_EXISTING_CUP_ID, 1, 2);
        assertThat(cupTeamsMatches, notNullValue());
        assertThat(cupTeamsMatches, hasSize(0));
    }

    @Test
    public void shouldReturnTeamMatchesSortedByBeginningTimeDesc() {
        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();

        CupEditDTO cup1 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        CupEditDTO cup2 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());
        CupEditDTO cup3 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pcs.getPcsId());

        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team3 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team4 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        MatchEditDTO cup1Match1 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .finished(false)
                .startingAt(LocalDateTime.of(2016, 5, 20, 19, 0))
                .build());
        MatchEditDTO cup1Match2 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team2.getTeamId(), team1.getTeamId())
                .finished(4, 3)
                .startingAt(LocalDateTime.of(2016, 5, 22, 19, 0))
                .build());
        MatchEditDTO cup1Match3 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team1.getTeamId(), team3.getTeamId())
                .finished(4, 3)
                .startingAt(LocalDateTime.of(2016, 5, 23, 15, 0))
                .build());
        MatchEditDTO cup2Match1 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team3.getTeamId(), team1.getTeamId())
                .finished(1, 2)
                .startingAt(LocalDateTime.of(2016, 5, 24, 0, 0))
                .build());
        MatchEditDTO cup2Match2 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .finished(false)
                .startingAt(LocalDateTime.of(2016, 5, 25, 13, 0))
                .build());

        List<MatchDTO> matches1 = MatchEndPointsHandler.getCupTeamsMatches(cup1.getCupId(), team1.getTeamId(), team2.getTeamId());
        assertThat(matches1, hasSize(2));
        ComparisonUtils.assertTheSame(matches1.get(0), cup1Match2);
        ComparisonUtils.assertTheSame(matches1.get(1), cup1Match1);

        List<MatchDTO> matches3 = MatchEndPointsHandler.getCupTeamsMatches(cup1.getCupId(), team2.getTeamId(), team1.getTeamId());
        assertThat(matches3, hasSize(2));
        ComparisonUtils.assertTheSame(matches3.get(0), cup1Match2);
        ComparisonUtils.assertTheSame(matches3.get(1), cup1Match1);

        List<MatchDTO> matches2 = MatchEndPointsHandler.getCupTeamsMatches(cup1.getCupId(), team1.getTeamId(), team3.getTeamId());
        assertThat(matches2, hasSize(1));
        ComparisonUtils.assertTheSame(matches2.get(0), cup1Match3);

        List<MatchDTO> matches4 = MatchEndPointsHandler.getCupTeamsMatches(cup1.getCupId(), team4.getTeamId(), team3.getTeamId());
        assertThat(matches4, notNullValue());
        assertThat(matches4, hasSize(0));

        List<MatchDTO> matches5 = MatchEndPointsHandler.getCupTeamsMatches(cup2.getCupId(), team1.getTeamId(), team3.getTeamId());
        assertThat(matches5, hasSize(1));
        ComparisonUtils.assertTheSame(matches5.get(0), cup2Match1);

        List<MatchDTO> matches6 = MatchEndPointsHandler.getCupTeamsMatches(cup2.getCupId(), team1.getTeamId(), team2.getTeamId());
        assertThat(matches6, hasSize(1));
        ComparisonUtils.assertTheSame(matches6.get(0), cup2Match2);

        List<MatchDTO> matches7 = MatchEndPointsHandler.getCupTeamsMatches(cup2.getCupId(), team2.getTeamId(), team1.getTeamId());
        assertThat(matches7, hasSize(1));
        ComparisonUtils.assertTheSame(matches7.get(0), cup2Match2);

        List<MatchDTO> matches8 = MatchEndPointsHandler.getCupTeamsMatches(cup3.getCupId(), team4.getTeamId(), team3.getTeamId());
        assertThat(matches8, notNullValue());
        assertThat(matches8, hasSize(0));

        List<MatchDTO> matches9 = MatchEndPointsHandler.getCupTeamsMatchesFinished(cup1.getCupId(), team1.getTeamId(), team2.getTeamId());
        assertThat(matches9, hasSize(1));
        ComparisonUtils.assertTheSame(matches9.get(0), cup1Match2);

        List<MatchDTO> matches10 = MatchEndPointsHandler.getCupTeamsMatchesFinished(cup1.getCupId(), team1.getTeamId(), team3.getTeamId());
        assertThat(matches10, hasSize(1));
        ComparisonUtils.assertTheSame(matches10.get(0), cup1Match3);

        List<MatchDTO> matches11 = MatchEndPointsHandler.getCupTeamsMatchesFinished(cup2.getCupId(), team1.getTeamId(), team3.getTeamId());
        assertThat(matches11, hasSize(1));
        ComparisonUtils.assertTheSame(matches11.get(0), cup2Match1);
    }
}
