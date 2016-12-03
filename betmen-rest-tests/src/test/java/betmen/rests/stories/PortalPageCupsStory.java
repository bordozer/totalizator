package betmen.rests.stories;

import betmen.dto.dto.admin.*;
import betmen.dto.dto.portal.PortalPageDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.apache.log4j.Logger;
import org.jbehave.core.annotations.*;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageCupsStory extends AbstractStory {

    private static final Logger LOGGER = Logger.getLogger(PortalPageCupsStory.class);

    private static final LocalDateTime PORTAL_PAGE_TIME = LocalDateTime.of(2016, 11, 30, 12, 13, 14);
    private static final String PORTAL_PAGE_DATE = DateTimeUtils.formatDate(PORTAL_PAGE_TIME.toLocalDate());

    private UserRegData userData1;
    private UserRegData userData2;

    private SportKindEditDTO sport;
    private PointsCalculationStrategyEditDTO pointsStrategy;

    private CategoryEditDTO favoriteCategory1;
    private CategoryEditDTO favoriteCategory2;
    private CategoryEditDTO notFavoriteCategory1;
    private CategoryEditDTO notFavoriteCategory2;

    private CupEditDTO favoriteCategory1PublicCurrentCup;
    private CupEditDTO favoriteCategory1PrivateCurrentCup;
    private CupEditDTO favoriteCategory2PublicCurrentCup;
    private CupEditDTO favoriteCategory2PrivateCurrentCup;
    private CupEditDTO notFavoriteCategory1PublicCurrentCup;
    private CupEditDTO notFavoriteCategory1PrivateCurrentCup;
    private CupEditDTO notFavoriteCategory1PublicCurrentNoMatchesCup;

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

    private TeamEditDTO teamNFC2_1;
    private TeamEditDTO teamNFC2_2;
    private TeamEditDTO teamNFC2_3;
    private TeamEditDTO teamNFC2_4;

    @BeforeScenario
    public void beforeScenario() {
        LOGGER.debug(this.getClass().getName());

        DataCleanUpUtils.cleanupAll();

        userData1 = RandomUtils.randomUser();
        AuthEndPointsHandler.registerNewUser(userData1);

        userData2 = RandomUtils.randomUser();
        AuthEndPointsHandler.registerNewUser(userData2);
    }

    @When("Admin logged in")
    public void iLoginAsAdmin() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    @When("$userName logged in")
    public void iLoginAsUser1(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData1);
    }

    @Then("User1 does not see any games on portal page")
    @Aliases(values = {"User1 still does not see any games on portal page"})
    public void thereAreNoGamesOnPortalPage() {
        assertNoPortalPageData();
    }

    @When("Admin creates categories and teams")
    public void adminCreatesCategoriesAndTeams() {
        sport = AdminTestDataGenerator.createSport();
        pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        favoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        favoriteCategory2 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        notFavoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        notFavoriteCategory2 = AdminTestDataGenerator.createCategory(sport.getSportKindId());

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

        teamNFC2_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
    }

    @Given("user1 is registered user")
    @Aliases(values = {"user2 is registered user"})
    public void ignore() {

    }

    @When("Admin creates cups")
    public void adminCreatesCups() {
        favoriteCategory1PublicCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).publicCup().future().build()));
        favoriteCategory1PrivateCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).privateCup().future().build()));

        favoriteCategory2PublicCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).publicCup().future().build()));
        favoriteCategory2PrivateCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).privateCup().future().build()));

        notFavoriteCategory1PublicCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).publicCup().future().build()));
        notFavoriteCategory1PrivateCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).privateCup().future().build()));

        notFavoriteCategory1PublicCurrentNoMatchesCup = createCup((cupTemplater(notFavoriteCategory2, pointsStrategy).privateCup().future().build()));
    }

    @When("$userName defines some favorite categories")
    public void user1DefinesSomeFavoriteCategories(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData1);
    }

    @When("Admin creates games on Date before")
    public void adminCreatesGamesOnDateBefore(@Named("userName") final String userName) {
        final LocalDateTime dayBefore = PORTAL_PAGE_TIME.minusDays(1);
        createMatch(favoriteCategory1PublicCurrentCup, teamFC1_1, teamFC1_2, dayBefore);
        createMatch(favoriteCategory1PublicCurrentCup, teamFC1_3, teamFC1_4, dayBefore);

        createMatch(favoriteCategory1PrivateCurrentCup, teamFC1_5, teamFC1_6, dayBefore);

        createMatch(favoriteCategory2PublicCurrentCup, teamFC2_1, teamFC2_2, dayBefore);
        createMatch(favoriteCategory2PublicCurrentCup, teamFC2_3, teamFC2_4, dayBefore);

        createMatch(favoriteCategory2PrivateCurrentCup, teamFC2_5, teamFC2_6, dayBefore);

        createMatch(notFavoriteCategory1PublicCurrentCup, teamNFC1_1, teamNFC1_2, dayBefore);
        createMatch(notFavoriteCategory1PublicCurrentCup, teamNFC1_3, teamNFC1_4, dayBefore);

        createMatch(notFavoriteCategory1PrivateCurrentCup, teamNFC1_5, teamNFC1_6, dayBefore);

        createMatch(notFavoriteCategory1PublicCurrentNoMatchesCup, teamNFC2_1, teamNFC2_2, dayBefore);
        createMatch(notFavoriteCategory1PublicCurrentNoMatchesCup, teamNFC2_3, teamNFC2_4, dayBefore);
    }

    private void createMatch(CupEditDTO cup, TeamEditDTO team1, TeamEditDTO team2, LocalDateTime beginningTime) {
        MatchEditDTO match1 = createMatch(matchTemplater(cup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .future()
                .withBeginningTime(beginningTime).build()
        );
    }

    private void assertNoPortalPageData() {
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(getPortalPageDTO());
        assertThat(responseDto, notNullValue());

        assertThat(responseDto.getCupsTodayToShow(), notNullValue());
        assertThat(responseDto.getCupsTodayToShow(), hasSize(0));

        assertThat(responseDto.getCupsToShow(), notNullValue());
        assertThat(responseDto.getCupsToShow(), hasSize(0));

        assertThat(responseDto.getAnotherMatchesOnDate(), notNullValue());
        assertThat(responseDto.getAnotherMatchesOnDate(), hasSize(0));
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
