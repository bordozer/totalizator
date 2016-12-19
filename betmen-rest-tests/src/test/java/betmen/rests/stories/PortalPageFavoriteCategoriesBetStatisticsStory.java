package betmen.rests.stories;

import betmen.dto.dto.admin.*;
import betmen.dto.dto.portal.FavoriteCategoriesBetStatisticsDTO;
import betmen.dto.dto.portal.FavoriteCategoryBetStatisticsDTO;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.jbehave.core.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageFavoriteCategoriesBetStatisticsStory extends AbstractCommonStepsStory {

    private static final LocalDateTime TIME = LocalDateTime.now();
    private static final LocalDate ON_DATE = TIME.toLocalDate();
    private static final LocalDateTime DATE_FIRST_SEC = LocalDateTime.of(TIME.getYear(), TIME.getMonth(), TIME.getDayOfMonth(), 0, 0, 0);
    private static final LocalDateTime DATE_LAST_SEC = LocalDateTime.of(TIME.getYear(), TIME.getMonth(), TIME.getDayOfMonth(), 23, 59, 59);

    private CategoryEditDTO category;
    private CupEditDTO publicCup;
    private CupEditDTO privateCup;
    private TeamEditDTO team1;
    private TeamEditDTO team2;
    private TeamEditDTO team3;
    private TeamEditDTO team4;

    private MatchEditDTO yesterdayMatch;
    private MatchEditDTO todayMatch;
    private MatchEditDTO anotherTodayMatch;
    private MatchEditDTO tomorrowMatch;

    @BeforeScenario
    public void beforeScenario() {
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        final PointsCalculationStrategyEditDTO strategy = AdminTestDataGenerator.createPointsStrategy();
        category = AdminTestDataGenerator.createCategory();
        publicCup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), strategy.getPcsId()).publicCup().future().build());
        privateCup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), strategy.getPcsId()).privateCup().future().build());
        team1 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
        team2 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
        team3 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
        team4 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
    }

    @Given("$userName deletes all matches")
    public void adminDeletesAllMatches(@Named("userName") final String userName) {
        DataCleanUpUtils.cleanupMatches();
    }

    @Then("Admin is seeing an empty statistics")
    public void userIsSeeingAnEmptyStatistics() {
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);
        assertNothingFound(dto);
    }

    @Then("Admin is seeing the category in statistics with one game on date but no bet")
    public void adminSeesCategoryInStatisticsButNoBet() {
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);
        final FavoriteCategoryBetStatisticsDTO statistics = dto.getCategoryBetStatistics().get(0);
        ComparisonUtils.assertTheSame(statistics.getCategory(), category);
        assertThat(statistics.getMatchesCount(), is(1));
        assertThat(statistics.getBetsCount(), is(0));
    }

    @When("Admin makes bet on today's game")
    public void adminMakesBetOnTodayGame() {
        BetEndPointsHandler.make(todayMatch.getMatchId(), 1, 2);
    }

    @When("Admin makes bet on another today's game")
    public void adminMakesBetOnAnotherTodayGame() {
        BetEndPointsHandler.make(anotherTodayMatch.getMatchId(), 1, 2);
    }

    @Then("Admin is seeing the category in statistics with one game on date and one bet")
    public void adminSeesCategoryWithOneGameInStatisticsAndBet() {
        assertStatistics(1, 1);
    }

    @Then("Admin is seeing the category in statistics with two game on date and one bet")
    public void adminSeesCategoryWithTwoGameInStatisticsAndOneBet() {
        assertStatistics(2, 1);
    }

    @Then("Admin is seeing the category in statistics with two game on date and two bet")
    public void adminSeesCategoryWithTwoGameInStatisticsAndTwoBets() {
        assertStatistics(2, 2);
    }

    @When("Admin creates two games - yesterday's and tomorrow's")
    public void adminCreatesTwoGamesYesterdaysAndTomorrow() {
        AuthEndPointsHandler.loginAsAdmin();
        yesterdayMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_FIRST_SEC.minusSeconds(1))
                .finished(true)
                .build());
        tomorrowMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_LAST_SEC.plusSeconds(1))
                .finished(false)
                .build());
    }

    @When("Admin creates one today's game")
    public void adminCreatesOneTodayGame() {
        todayMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(TIME.plusHours(1))
                .finished(false)
                .build());
    }

    @When("Admin creates another today's game")
    public void adminCreatesAnotherTodayGame() {
        anotherTodayMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team3.getTeamId(), team4.getTeamId())
                .builder()
                .withBeginningTime(TIME.plusHours(1))
                .finished(false)
                .build());
    }

    @When("Admin adds the category to favorites")
    public void userAddsCategoryToFavorites() {
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());
    }

    @Given("There are no games in the system")
    public void ignore() {

    }

    private void assertStatistics(int matchesCount, int betsCount) {
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        assertThat(dto, notNullValue());
        assertThat(dto.getOnDate(), is(ON_DATE));
        assertThat(dto.getCategoryBetStatistics(), notNullValue());
        assertThat(dto.getCategoryBetStatistics(), hasSize(1));

        final FavoriteCategoryBetStatisticsDTO statistics = dto.getCategoryBetStatistics().get(0);
        ComparisonUtils.assertTheSame(statistics.getCategory(), category);
        assertThat(statistics.getMatchesCount(), is(matchesCount));
        assertThat(statistics.getBetsCount(), is(betsCount));
    }

    private void assertNothingFound(final FavoriteCategoriesBetStatisticsDTO dto) {
        assertThat(dto, notNullValue());
        assertThat(dto.getOnDate(), is(ON_DATE));
        assertThat(dto.getCategoryBetStatistics(), notNullValue());
        assertThat(dto.getCategoryBetStatistics(), hasSize(0));
    }
}
