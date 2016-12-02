package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.portal.PortalPageDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.MatchEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageFlowRestTest {

    private static final Logger LOGGER = Logger.getLogger(PortalPageFlowRestTest.class);

    private static final LocalDateTime PORTAL_PAGE_TIME = LocalDateTime.of(2016, 11, 31, 12, 13, 14);
    private static final String TODAY = DateTimeUtils.formatDate(PORTAL_PAGE_TIME.toLocalDate());

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

    private TeamEditDTO teamFC2_1;
    private TeamEditDTO teamFC2_3;
    private TeamEditDTO teamFC2_2;
    private TeamEditDTO teamFC2_4;

    private TeamEditDTO teamNFC1_1;
    private TeamEditDTO teamNFC1_2;
    private TeamEditDTO teamNFC1_3;
    private TeamEditDTO teamNFC1_4;

    private TeamEditDTO teamNFC2_1;
    private TeamEditDTO teamNFC2_2;
    private TeamEditDTO teamNFC2_3;
    private TeamEditDTO teamNFC2_4;

    @BeforeClass
    public void initClass() {
        LOGGER.debug(this.getClass().getName());

        DataCleanUpUtils.cleanupAll();

        userData1 = RandomUtils.randomUser();
        UserDTO user1 = AuthEndPointsHandler.registerNewUser(userData1);

        userData2 = RandomUtils.randomUser();
        UserDTO user2 = AuthEndPointsHandler.registerNewUser(userData1);

        /*AuthEndPointsHandler.loginAsAdmin();

        sport = AdminTestDataGenerator.createSport();
        pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        favoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        favoriteCategory2 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        notFavoriteCategory1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        notFavoriteCategory2 = AdminTestDataGenerator.createCategory(sport.getSportKindId());

        favoriteCategory1PublicCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).publicCup().future().build()));
        favoriteCategory1PrivateCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).privateCup().future().build()));
        favoriteCategory2PublicCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).publicCup().future().build()));
        favoriteCategory2PrivateCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).privateCup().future().build()));
        notFavoriteCategory1PublicCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).publicCup().future().build()));
        notFavoriteCategory1PrivateCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).privateCup().future().build()));
        notFavoriteCategory1PublicCurrentNoMatchesCup = createCup((cupTemplater(notFavoriteCategory2, pointsStrategy).privateCup().future().build()));

        TeamEditDTO teamFC1_1 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        TeamEditDTO teamFC1_2 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        TeamEditDTO teamFC1_3 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());
        TeamEditDTO teamFC1_4 = AdminTestDataGenerator.createTeam(favoriteCategory1.getCategoryId());

        TeamEditDTO teamFC2_1 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        TeamEditDTO teamFC2_2 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        TeamEditDTO teamFC2_3 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        TeamEditDTO teamFC2_4 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());

        TeamEditDTO teamNFC1_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        TeamEditDTO teamNFC1_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        TeamEditDTO teamNFC1_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        TeamEditDTO teamNFC1_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());

        TeamEditDTO teamNFC2_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        TeamEditDTO teamNFC2_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        TeamEditDTO teamNFC2_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        TeamEditDTO teamNFC2_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());

        MatchEditDTO match11 = createMatch(matchTemplater(cup.getCupId(), teamFC1_1.getTeamId(), teamFC1_2.getTeamId()).future().withBeginningTime(NOW.plusDays(1)).build());*/
    }

    @Test(priority = 10)
    public void thereIsNoDataInTheSystem_NoDataOnPortalPage() {
        // no games, no cups on PP
        AuthEndPointsHandler.login(userData1);
        assertNoPortalPageData();
    }

    @Test(priority = 20)
    public void adminCreatesCategoriesAndTeams_NoDataOnPortalPage() {
        AuthEndPointsHandler.loginAsAdmin();
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

        teamFC2_1 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_2 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_3 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());
        teamFC2_4 = AdminTestDataGenerator.createTeam(favoriteCategory2.getCategoryId());

        teamNFC1_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());
        teamNFC1_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory1.getCategoryId());

        teamNFC2_1 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_2 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_3 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());
        teamNFC2_4 = AdminTestDataGenerator.createTeam(notFavoriteCategory2.getCategoryId());

        // no games, no cups on PP
        AuthEndPointsHandler.login(userData1);
        assertNoPortalPageData();
    }

    @Test(priority = 30)
    public void adminCreatesCups_NoDataOnPortalPage() {
        AuthEndPointsHandler.loginAsAdmin();
        favoriteCategory1PublicCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).publicCup().future().build()));
        favoriteCategory1PrivateCurrentCup = createCup((cupTemplater(favoriteCategory1, pointsStrategy).privateCup().future().build()));
        favoriteCategory2PublicCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).publicCup().future().build()));
        favoriteCategory2PrivateCurrentCup = createCup((cupTemplater(favoriteCategory2, pointsStrategy).privateCup().future().build()));
        notFavoriteCategory1PublicCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).publicCup().future().build()));
        notFavoriteCategory1PrivateCurrentCup = createCup((cupTemplater(notFavoriteCategory1, pointsStrategy).privateCup().future().build()));
        notFavoriteCategory1PublicCurrentNoMatchesCup = createCup((cupTemplater(notFavoriteCategory2, pointsStrategy).privateCup().future().build()));

        // still no games, no cups on PP
        AuthEndPointsHandler.login(userData1);
        assertNoPortalPageData();
    }

    @Test(priority = 40)
    public void user1DefinedFavoriteCategories_NoDataOnPortalPage() {
        AuthEndPointsHandler.login(userData1);
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory1PublicCurrentCup.getCategoryId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory1PrivateCurrentCup.getCategoryId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory2PublicCurrentCup.getCategoryId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory2PrivateCurrentCup.getCategoryId());

        // still no games, no cups on PP
        AuthEndPointsHandler.login(userData1);
        assertNoPortalPageData();
    }

    @Test(priority = 50)
    public void adminCreatedOneGameInFavoriteCategoryOnPPDate() {
        AuthEndPointsHandler.loginAsAdmin();
//        AdminMatchEndPointsHandler.create()
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
        dto.setPortalPageDate(TODAY);
        return dto;
    }
}
