package betmen.rests.stories;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.portal.PortalPageDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

@Slf4j
public class PortalPageCupsStory extends AbstractCommonStepsStory {

    private static final LocalDateTime PORTAL_PAGE_TIME = LocalDateTime.of(2016, 11, 30, 12, 13, 14);
    private static final String PORTAL_PAGE_DATE = DateTimeUtils.formatDate(PORTAL_PAGE_TIME.toLocalDate());

    private UserRegData userData1;
    private UserRegData userData2;

    private SportKindEditDTO sport;
    private PointsCalculationStrategyEditDTO pointsStrategy;

    private CategoryEditDTO favoriteCategory1;
    private CategoryEditDTO favoriteCategory2;
    private CategoryEditDTO notFavoriteCategory1;

    private CupEditDTO user1FavoriteCategory1PublicCurrentCup;
    private CupEditDTO favoriteCategory1PrivateCurrentCup;
    private CupEditDTO user1FavoriteCategory2PublicCurrentCup;
    private CupEditDTO favoriteCategory2PrivateCurrentCup;
    private CupEditDTO notFavoriteCategory1PublicCurrentCup;
    private CupEditDTO notFavoriteCategory1PrivateCurrentCup;

    private TeamEditDTO teamFC1_1;
    private TeamEditDTO teamFC1_2;
    private TeamEditDTO teamFC1_3;
    private TeamEditDTO teamFC1_4;
    private TeamEditDTO teamFC1_5;
    private TeamEditDTO teamFC1_6;

    private TeamEditDTO teamFC2_1;
    private TeamEditDTO teamFC2_3;
    private TeamEditDTO teamFC2_2;
    private TeamEditDTO teamFC2_4;
    private TeamEditDTO teamFC2_5;
    private TeamEditDTO teamFC2_6;

    private TeamEditDTO teamNFC1_1;
    private TeamEditDTO teamNFC1_2;
    private TeamEditDTO teamNFC1_3;
    private TeamEditDTO teamNFC1_4;
    private TeamEditDTO teamNFC1_5;
    private TeamEditDTO teamNFC1_6;

    @BeforeScenario
    public void beforeScenario() {
        DataCleanUpUtils.cleanupAll();

        userData1 = RandomUtils.randomUser();
        AuthEndPointsHandler.registerNewUser(userData1);

        userData2 = RandomUtils.randomUser();
        AuthEndPointsHandler.registerNewUser(userData2);
    }

    @When("User $userName logged in")
    public void user1LoggedIn(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData1);
    }

    @When("Another user $userName logged in")
    public void anotherUser2LoggedIn(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData2);
    }

    @Then("$userName does not see any games on portal page")
    @Aliases(values = {"$userName still does not see any games on portal page"})
    public void thereAreNoGamesOnPortalPage(@Named("userName") final String userName) {
        assertNoPortalPageData();
    }

    @Then("$userName still does not see any games on portal page but can see favorite category cup statistic")
    public void thereAreNoGamesOnPortalPageButOneFavoriteCategoryCupStatistic(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(0));
        assertThat(data.getCupsToShow(), hasSize(1));
        assertThat(data.getAnotherMatchesOnDate(), hasSize(0));
    }

    @When("Admin creates categories and teams")
    public void adminCreatesCategoriesAndTeams() {
        sport = AdminTestDataGenerator.createSport();
        pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        favoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        favoriteCategory2 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        notFavoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());

        teamFC1_1 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        teamFC1_2 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        teamFC1_3 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        teamFC1_4 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        teamFC1_5 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        teamFC1_6 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());

        teamFC2_1 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_2 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_3 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_4 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_5 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_6 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());

        teamNFC1_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_5 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_6 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
    }

    @When("Admin creates cups")
    public void adminCreatesCups() {
        user1FavoriteCategory1PublicCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).publicCup().inDays(3).build()));
        favoriteCategory1PrivateCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).privateCup().inDays(2).build()));

        user1FavoriteCategory2PublicCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).publicCup().inDays(5).build()));
        favoriteCategory2PrivateCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).privateCup().inDays(1).build()));

        notFavoriteCategory1PublicCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).publicCup().inDays(10).build()));
        notFavoriteCategory1PrivateCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).privateCup().inDays(4).build()));
    }

    @When("$userName defines one favorite category")
    public void user1DefinesSomeFavoriteCategories(@Named("userName") final String userName) {
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory1.getCategoryId());
    }

    @When("Admin creates games on Date before")
    public void adminCreatesGamesOnDateBefore() {
        createMatchesOnDate(PORTAL_PAGE_TIME.minusDays(1));
    }

    @When("Admin creates games on Date after")
    public void adminCreatesGamesOnDateAfter() {
        createMatchesOnDate(PORTAL_PAGE_TIME.plusDays(1));
    }

    @When("Admin adds one game on Portal Page Date in $userName's favorite category")
    public void adminCreatesGamesOnPortalPageDate(@Named("userName") final String userName) {
        createMatch(user1FavoriteCategory1PublicCurrentCup, teamFC1_1, teamFC1_2, PORTAL_PAGE_TIME.plusMinutes(5));
    }

    @Then("$userName can see the one cup of favorite category and cup's short statistics on Portal page")
    public void canSeeGameOfCupOfFavoriteCategoryAndCupShortStatistics(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(0), user1FavoriteCategory1PublicCurrentCup);

        assertThat(data.getCupsToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(0), user1FavoriteCategory1PublicCurrentCup);

        assertThat(data.getAnotherMatchesOnDate(), hasSize(0));
    }

    @When("Admin adds one more game on Portal Page Date in $userName's favorite category")
    public void adminCreatesAnotherGamesOnPortalPageDate(@Named("userName") final String userName) {
        createMatch(user1FavoriteCategory1PublicCurrentCup, teamFC1_3, teamFC1_4, PORTAL_PAGE_TIME.plusMinutes(5));
    }

    @When("Admin adds one more game on Portal Page Date in category not favorite of $userName")
    public void adminCreatesAnotherGamesOnPortalPageDateToUser1NotFavoriteCategory(@Named("userName") final String userName) {
        createMatch(notFavoriteCategory1PublicCurrentCup, teamNFC1_5, teamNFC1_6, PORTAL_PAGE_TIME.plusMinutes(5));
    }

    @Then("$userName can see the one cup of favorite category and cup's short statistics on Portal page and one cup in Another section")
    public void canSeeGameOfCupOfFavoriteCategoryAndCupShortStatisticsAndOneCupInAnotherSection(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(0), user1FavoriteCategory1PublicCurrentCup);

        assertThat(data.getCupsToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(data.getCupsToShow().get(0), user1FavoriteCategory1PublicCurrentCup);

        assertThat(data.getAnotherMatchesOnDate(), hasSize(1));
        assertThat(data.getAnotherMatchesOnDate().get(0).getCategories().get(0).getCups().get(0).getCup().getCupId(), is(notFavoriteCategory1PublicCurrentCup.getCupId()));
    }

    @Then("$userName can see only favorite category statistics")
    public void userCanSeeOnlyFavoriteCategoryStatistics(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(0));

        assertThat("Favorite category's cup should be shown", data.getCupsToShow(), hasSize(1));
        assertThat("Cup should not be null", data.getCupsToShow(), notNullValue());
        ComparisonUtils.assertTheSame(data.getCupsToShow().get(0), user1FavoriteCategory1PublicCurrentCup);

        assertThat(data.getAnotherMatchesOnDate(), hasSize(0));
    }

    @When("Admin adds games on Portal Page Date in private cup of favorite category")
    public void adminCreatesGamesInPrivateCupOfFavoriteCategory() {
        createMatch(favoriteCategory1PrivateCurrentCup, teamFC1_5, teamFC1_6, PORTAL_PAGE_TIME.plusMinutes(4));
    }

    @When("Admin adds games on Portal Page Date in private cup of NOT favorite category")
    public void adminCreatesGamesInPrivateCupOfNotFavoriteCategory() {
        createMatch(notFavoriteCategory1PrivateCurrentCup, teamNFC1_1, teamNFC1_2, PORTAL_PAGE_TIME.plusMinutes(4));
    }

    @When("Admin adds games on Portal Page Date in another public cup")
    public void adminCreatesGamesOnDateInAnotherPublicCup() {
        createMatch(user1FavoriteCategory2PublicCurrentCup, teamFC2_1, teamFC2_2, PORTAL_PAGE_TIME.plusMinutes(7));
        createMatch(user1FavoriteCategory2PublicCurrentCup, teamFC2_3, teamFC2_4, PORTAL_PAGE_TIME.minusMinutes(4));
    }

    @When("User $userName adds another category to favorites")
    public void user1AddsAnotherCategoryToFavorites(@Named("userName") final String userName) {
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory2.getCategoryId());
    }

    @Then("$userName can see the two cups of favorite categories and cup's short statistics on Portal page and one cup in Another section")
    public void canSeeGameOfCupOfTwoFavoriteCategoriesAndCupShortStatisticsAndOneCupInAnotherSection(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(2));
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(0), user1FavoriteCategory1PublicCurrentCup);
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(1), user1FavoriteCategory2PublicCurrentCup);

        assertThat(data.getCupsToShow(), hasSize(2));
        ComparisonUtils.assertTheSame(data.getCupsToShow().get(0), user1FavoriteCategory1PublicCurrentCup);
        ComparisonUtils.assertTheSame(data.getCupsToShow().get(1), user1FavoriteCategory2PublicCurrentCup);

        assertThat(data.getAnotherMatchesOnDate(), hasSize(1));
        assertThat(data.getAnotherMatchesOnDate().get(0).getCategories().get(0).getCups().get(0).getCup().getCupId(), is(notFavoriteCategory1PublicCurrentCup.getCupId()));
    }

    @Then("$userName does not see cups in match section and cup statistics section")
    public void user2DoesNotSeeCupsInMatchSectionAndCupStatisticsSection(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(0));

        assertThat(data.getCupsToShow(), hasSize(0));

        assertThat(data.getAnotherMatchesOnDate(), hasSize(1));
        assertThat(data.getAnotherMatchesOnDate().get(0).getCategories().get(0).getCups().get(0).getCup().getCupId(), is(user1FavoriteCategory1PublicCurrentCup.getCupId()));
    }

    @When("Admin finish current cup from favorite category")
    public void user2DoesNotSeeCupsInMatchSectionAndCupStatisticsSection() {
        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, teamFC1_1.getTeamId()));
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(2, teamFC1_2.getTeamId()));
        user1FavoriteCategory1PublicCurrentCup.setCupWinners(cupWinners);
        user1FavoriteCategory1PublicCurrentCup = AdminCupEndPointsHandler.update(user1FavoriteCategory1PublicCurrentCup);
    }

    @Then("$userName can not see finished cup in statistics section and can see it in favorite categories cups section")
    public void user1CanNotSeeFinishedCupInStatisticsSectionAndCanSeeItInFavoriteCategoriesCupsSection(@Named("userName") final String userName) {
        PortalPageDTO data = getPortalPageData();
        assertThat(data.getCupsTodayToShow(), hasSize(2));
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(0), user1FavoriteCategory1PublicCurrentCup);
        ComparisonUtils.assertTheSame(data.getCupsTodayToShow().get(1), user1FavoriteCategory2PublicCurrentCup);

        assertThat(data.getCupsToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(data.getCupsToShow().get(0), user1FavoriteCategory2PublicCurrentCup);

        assertThat(data.getAnotherMatchesOnDate(), hasSize(1));
        assertThat(data.getAnotherMatchesOnDate().get(0).getCategories().get(0).getCups().get(0).getCup().getCupId(), is(notFavoriteCategory1PublicCurrentCup.getCupId()));
    }

    @Given("user1 is registered user")
    @Aliases(values = {"user2 is registered user"})
    public void ignore() {

    }

    private PortalPageDTO getPortalPageData() {
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(PORTAL_PAGE_DATE);
        return PortalPageEndPointHandler.getPortalPageCups(dto);
    }

    private void createMatchesOnDate(final LocalDateTime dayBefore) {
        createMatch(user1FavoriteCategory1PublicCurrentCup, teamFC1_1, teamFC1_2, dayBefore);
        createMatch(user1FavoriteCategory1PublicCurrentCup, teamFC1_3, teamFC1_4, dayBefore);

        createMatch(favoriteCategory1PrivateCurrentCup, teamFC1_5, teamFC1_6, dayBefore);

        createMatch(user1FavoriteCategory2PublicCurrentCup, teamFC2_1, teamFC2_2, dayBefore);
        createMatch(user1FavoriteCategory2PublicCurrentCup, teamFC2_3, teamFC2_4, dayBefore);

        createMatch(favoriteCategory2PrivateCurrentCup, teamFC2_5, teamFC2_6, dayBefore);

        createMatch(notFavoriteCategory1PublicCurrentCup, teamNFC1_1, teamNFC1_2, dayBefore);
        createMatch(notFavoriteCategory1PublicCurrentCup, teamNFC1_3, teamNFC1_4, dayBefore);

        createMatch(notFavoriteCategory1PrivateCurrentCup, teamNFC1_5, teamNFC1_6, dayBefore);
    }

    private MatchEditDTO createMatch(CupEditDTO cup, TeamEditDTO team1, TeamEditDTO team2, LocalDateTime beginningTime) {
        return createMatch(matchTemplater(cup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .future()
                .withBeginningTime(beginningTime).build()
        );
    }

    private void assertNoPortalPageData() {
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(getPortalPageDTO());
        assertThat(responseDto, notNullValue());

        assertThat(responseDto.getCupsTodayToShow(), notNullValue());
        assertThat("Should not be cups of favorite categories", responseDto.getCupsTodayToShow(), hasSize(0));

        assertThat(responseDto.getCupsToShow(), notNullValue());
        assertThat("Should not be cup statistics of favorite categories", responseDto.getCupsToShow(), hasSize(0));

        assertThat(responseDto.getAnotherMatchesOnDate(), notNullValue());
        assertThat("Should not be cups in Another games section", responseDto.getAnotherMatchesOnDate(), hasSize(0));
    }

    private CupTemplater cupTemplater(final CategoryEditDTO category, final PointsCalculationStrategyEditDTO ps) {
        return CupTemplater.random(category.getCategoryId(), ps.getPcsId());
    }

    private CupEditDTO createCup(final CupEditDTO cup) {
        return AdminCupEndPointsHandler.create(cup);
    }

    private static MatchTemplater matchTemplater(final int cupId, final int team1Id, final int team2Id) {
        return MatchTemplater.random(cupId, team1Id, team2Id);
    }

    public static MatchEditDTO createMatch(final MatchEditDTO matchEditDTO) {
        return AdminMatchEndPointsHandler.create(matchEditDTO);
    }

    private PortalPageDTO getPortalPageDTO() {
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(PORTAL_PAGE_DATE);
        return dto;
    }
}
