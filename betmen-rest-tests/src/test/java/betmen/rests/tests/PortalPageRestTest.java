package betmen.rests.tests;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.PortalPageDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageRestTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final String TODAY = DateTimeUtils.formatDate(NOW.toLocalDate());

    private SportKindEditDTO sport;
    private PointsCalculationStrategyEditDTO pointsStrategy;

    @BeforeClass
    public void initClass() {
        AuthEndPointsHandler.loginAsAdmin();
        sport = AdminTestDataGenerator.createSport();
        pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetNoCupsForPPIfNoFavoritesCategories() {
        AuthEndPointsHandler.loginAsAdmin();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cup = createCup((cupTemplater(category, pointsStrategy).publicCup().future().build()));

        AuthEndPointsHandler.registerNewUserAndLogin();

        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(TODAY);
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        assertThat(responseDto, notNullValue());

        // Games of cups of user's favorites categories + games where user made a bet on today's games
        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(0));

        // favorite cups only
        List<CupDTO> cupsToShow = responseDto.getCupsToShow();
        assertThat(cupsToShow, notNullValue());
        assertThat(cupsToShow, hasSize(0));
    }

    @Test
    public void shouldGetCupsFromFavoritesIfThereAreNoGames() {
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO category = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cup = createCup((cupTemplater(category, pointsStrategy).publicCup().future().build()));

        CategoryEditDTO category1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        createCup((cupTemplater(category1, pointsStrategy).publicCup().future().build()));

        AuthEndPointsHandler.registerNewUserAndLogin();

        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());

        PortalPageDTO dto = getPortalPageDTO();
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        // Games of cups of user's favorites categories + games where user made a bet on today's games
        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(1));
        ComparisonUtils.assertTheSame(cupsTodayToShow.get(0), cup);

        // favorite cups only
        List<CupDTO> cupsToShow = responseDto.getCupsToShow();
        assertThat(cupsToShow, notNullValue());
        assertThat(cupsToShow, hasSize(1));
        ComparisonUtils.assertTheSame(cupsToShow.get(0), cup);
    }

    @Test
    public void shouldShowCupsNotFromFavoritesIfUserMadeABetOnAGameOfCupForPPDate() {
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO category = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cup = createCup((cupTemplater(category, pointsStrategy).publicCup().future().build()));

        TeamEditDTO team11 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team12 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        MatchEditDTO match = createMatch(matchTemplater(cup.getCupId(), team11.getTeamId(), team12.getTeamId()).future().withBeginningTime(NOW.plusMinutes(2)).build());

        AuthEndPointsHandler.registerNewUserAndLogin();

        BetEndPointsHandler.make(match.getMatchId(), 1, 2);

        PortalPageDTO dto = getPortalPageDTO();
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        // Games of cups of user's favorites categories + games where user made a bet on today's games
        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(1));

        ComparisonUtils.assertTheSame(cupsTodayToShow.get(0), cup);

        // favorite cups only
        List<CupDTO> cupsToShow = responseDto.getCupsToShow();
        assertThat(cupsToShow, notNullValue());
        assertThat(cupsToShow, hasSize(0));
    }

    @Test
    public void shouldShowCupsNotFromFavoritesIfUserMadeABetOnAGameOfCupForPPDateAndFavoritesCategoriesCups() {
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO favoriteCategory = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cupOfFavoriteCategory = createCup((cupTemplater(favoriteCategory, pointsStrategy).publicCup().future().build()));
        TeamEditDTO team11 = AdminTestDataGenerator.createTeam(favoriteCategory.getCategoryId());
        TeamEditDTO team12 = AdminTestDataGenerator.createTeam(favoriteCategory.getCategoryId());
        MatchEditDTO match11 = createMatch(matchTemplater(cupOfFavoriteCategory.getCupId(), team11.getTeamId(), team12.getTeamId()).future().withBeginningTime(NOW.plusMinutes(2)).build());
        MatchEditDTO match12 = createMatch(matchTemplater(cupOfFavoriteCategory.getCupId(), team12.getTeamId(), team11.getTeamId()).future().withBeginningTime(NOW.plusDays(1)).build());

        CategoryEditDTO category = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cupWithGameWithABetToday = createCup((cupTemplater(category, pointsStrategy).publicCup().future().build()));
        TeamEditDTO team21 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team22 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        MatchEditDTO match21 = createMatch(matchTemplater(cupWithGameWithABetToday.getCupId(), team21.getTeamId(), team22.getTeamId()).future().withBeginningTime(NOW.plusMinutes(2)).build());

        CategoryEditDTO category3 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cup3 = createCup((cupTemplater(category3, pointsStrategy).publicCup().future().build()));
        TeamEditDTO team31 = AdminTestDataGenerator.createTeam(category3.getCategoryId());
        TeamEditDTO team32 = AdminTestDataGenerator.createTeam(category3.getCategoryId());
        MatchEditDTO match31 = createMatch(matchTemplater(cup3.getCupId(), team31.getTeamId(), team32.getTeamId()).future().withBeginningTime(NOW.plusMinutes(5)).build());

        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(favoriteCategory.getCategoryId());

        BetEndPointsHandler.make(match21.getMatchId(), 1, 2);

        PortalPageDTO dto = getPortalPageDTO();
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        // Games of cups of user's favorites categories + games where user made a bet on today's games
        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(2));
        ComparisonUtils.assertTheSame(cupsTodayToShow.get(0), cupOfFavoriteCategory);
        ComparisonUtils.assertTheSame(cupsTodayToShow.get(1), cupWithGameWithABetToday);

        // favorite cups only
        List<CupDTO> cupsToShow = responseDto.getCupsToShow();
        assertThat(cupsToShow, notNullValue());
        assertThat(cupsToShow, hasSize(1));
        ComparisonUtils.assertTheSame(cupsToShow.get(0), cupOfFavoriteCategory);
    }

    @Test
    public void shouldNotShowNotFavoriteCategoryCupOnPPIfNoBetForToday() {
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO favoriteCategory = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        CupEditDTO cup = createCup((cupTemplater(favoriteCategory, pointsStrategy).publicCup().future().build()));
        TeamEditDTO team11 = AdminTestDataGenerator.createTeam(favoriteCategory.getCategoryId());
        TeamEditDTO team12 = AdminTestDataGenerator.createTeam(favoriteCategory.getCategoryId());
        MatchEditDTO match11 = createMatch(matchTemplater(cup.getCupId(), team11.getTeamId(), team12.getTeamId()).future().withBeginningTime(NOW.plusDays(1)).build());
        MatchEditDTO match12 = createMatch(matchTemplater(cup.getCupId(), team12.getTeamId(), team11.getTeamId()).future().withBeginningTime(NOW.plusDays(2)).build());
        MatchEditDTO match13 = createMatch(matchTemplater(cup.getCupId(), team12.getTeamId(), team11.getTeamId()).future().withBeginningTime(NOW.plusDays(3)).build());

        AuthEndPointsHandler.registerNewUserAndLogin();

        BetEndPointsHandler.make(match11.getMatchId(), 4, 1);
        BetEndPointsHandler.make(match13.getMatchId(), 2, 2);

        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(DateTimeUtils.formatDate(NOW.plusDays(2)));
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        // Games of cups of user's favorites categories + games where user made a bet on today's games
        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(0));

        // favorite cups only
        List<CupDTO> cupsToShow = responseDto.getCupsToShow();
        assertThat(cupsToShow, notNullValue());
        assertThat(cupsToShow, hasSize(0));

        BetEndPointsHandler.make(match12.getMatchId(), 5, 7);

        PortalPageDTO responseDto1 = PortalPageEndPointHandler.getPortalPageCups(dto);
        assertThat(responseDto1, notNullValue());

        List<CupDTO> cupsTodayToShow1 = responseDto1.getCupsTodayToShow();
        assertThat(cupsTodayToShow1, hasSize(1));
        ComparisonUtils.assertTheSame(cupsTodayToShow1.get(0), cup);
    }

    private PortalPageDTO getPortalPageDTO() {
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(TODAY);
        return dto;
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
}
