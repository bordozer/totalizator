package betmen.rests.tests;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageFavoriteCategoriesBetStatisticsRestTest extends AbstractCleanableRestTest {

    private static final LocalDateTime TIME = LocalDateTime.of(2016, 12, 1, 12, 15, 0);
    private static final LocalDate ON_DATE = TIME.toLocalDate();
    private static final LocalDateTime DATE_FIRST_SEC = LocalDateTime.of(TIME.getYear(), TIME.getMonth(), TIME.getDayOfMonth(), 0, 0, 0);
    private static final LocalDateTime DATE_LAST_SEC = LocalDateTime.of(TIME.getYear(), TIME.getMonth(), TIME.getDayOfMonth(), 23, 59, 59);

    private CategoryEditDTO category;
    private CupEditDTO publicCup;
    private TeamEditDTO team1;
    private TeamEditDTO team2;

    @Override
    protected void beforeTest() {
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();

        final PointsCalculationStrategyEditDTO strategy = AdminTestDataGenerator.createPointsStrategy();
        category = AdminTestDataGenerator.createCategory();
        publicCup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), strategy.getPcsId()).publicCup().future().build());
        team1 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
        team2 = AdminTestDataGenerator.createTeam(publicCup.getCategoryId());
    }

    @BeforeMethod
    public void beforeMethod() {
        DataCleanUpUtils.cleanupMatches();
    }

    @Test
    public void shouldReturnEmptyListIfFavoriteCategoriesHaveNoMatches() {
        // given
        AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        // then
        assertNothingFound(dto);
    }

    @Test
    public void shouldReturnEmptyListIfNoMatchesOnDate() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_FIRST_SEC.minusSeconds(1))
                .build());
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_LAST_SEC.plusSeconds(1))
                .build());

        // when
        AuthEndPointsHandler.registerNewUserAndLogin();
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        // then
        assertNothingFound(dto);
    }

    @Test
    public void shouldReturnEmptyListIfFavoriteCategoriesHaveNoMatchesOnDate() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_FIRST_SEC.plusHours(1))
                .build());

        // when
        AuthEndPointsHandler.registerNewUserAndLogin();
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        // then
        assertNothingFound(dto);
    }

    @Test
    public void shouldReturnEmptyListIfUserHasFavoriteCategoryButItHaveNoMatchesOnDate() {
        // given

        // when
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        // then
        assertNothingFound(dto);
    }

    @Test
    public void shouldReturnMatchCountOfFavoriteCategoryIfItHasMatchesOnDate() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(DATE_FIRST_SEC.plusHours(1))
                .build());

        // when
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(ON_DATE);

        // then
        assertThat(dto, notNullValue());
        assertThat(dto.getOnDate(), is(ON_DATE));
        assertThat(dto.getCategoryBetStatistics(), notNullValue());
        assertThat(dto.getCategoryBetStatistics(), hasSize(1));

        final FavoriteCategoryBetStatisticsDTO statistics = dto.getCategoryBetStatistics().get(0);
        ComparisonUtils.assertTheSame(statistics.getCategory(), category);
        assertThat(statistics.getMatchesCount(), is(1));
        assertThat(statistics.getBetsCount(), is(0));
    }

    @Test
    public void shouldReturnMatchCountOfFavoriteCategoryAndBetOnItIfItHasMatchesOnDate() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate onDate = now.toLocalDate();
        AuthEndPointsHandler.loginAsAdmin();
        final MatchEditDTO match = AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .builder()
                .withBeginningTime(now.plusHours(1))
                .build());

        // when
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());
        BetEndPointsHandler.make(match.getMatchId(), 1, 2);
        final FavoriteCategoriesBetStatisticsDTO dto = PortalPageEndPointHandler.getPortalPageFavoritesCategoriesBetsStatistics(onDate);

        // then
        assertThat(dto, notNullValue());
        assertThat(dto.getOnDate(), is(onDate));
        assertThat(dto.getCategoryBetStatistics(), notNullValue());
        assertThat(dto.getCategoryBetStatistics(), hasSize(1));

        final FavoriteCategoryBetStatisticsDTO statistics = dto.getCategoryBetStatistics().get(0);
        ComparisonUtils.assertTheSame(statistics.getCategory(), category);
        assertThat(statistics.getMatchesCount(), is(1));
        assertThat(statistics.getBetsCount(), is(1));
    }

    private void assertNothingFound(FavoriteCategoriesBetStatisticsDTO dto) {
        assertThat(dto, notNullValue());
        assertThat(dto.getOnDate(), is(ON_DATE));
        assertThat(dto.getCategoryBetStatistics(), notNullValue());
        assertThat(dto.getCategoryBetStatistics(), hasSize(0));
    }
}
