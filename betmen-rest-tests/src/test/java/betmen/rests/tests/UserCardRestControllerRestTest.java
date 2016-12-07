package betmen.rests.tests;

import betmen.dto.dto.UserCardDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.UserCardEndPointsHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class UserCardRestControllerRestTest {

    private static final LocalDateTime USER_CARD_TIME_DATE = LocalDateTime.now();
    private static final LocalDate ON_DATE = USER_CARD_TIME_DATE.toLocalDate();

    @BeforeTest
    public void initTest() {
        DataCleanUpUtils.cleanupAll();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldReturn404ForAnonymousUser() {
        UserCardEndPointsHandler.getUserCardOnDate(0, LocalDate.now(), ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnprocessableEntityIfUserDoesNotExist() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserCardEndPointsHandler.getUserCardOnDate(548885, LocalDate.now(), ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldReturnBadRequestIfDateIsNotProvided() {
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();
        UserCardEndPointsHandler.getUserCardOnDate(user.getUserId(), null, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldGetOwnCardEmptyData() {
        // given
        UserDTO userDTO = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        UserCardDTO userCardData = UserCardEndPointsHandler.getUserCardOnDate(userDTO.getUserId(), LocalDate.now());

        //then
        assertThat(userCardData, notNullValue());
        assertThat(userCardData.getCupsToShow(), notNullValue());
        assertThat(userCardData.getCupsToShow(), hasSize(0));
    }

    @Test
    public void shouldGetAnotherUserCardEmptyData() {
        // given
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUser();
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        UserCardDTO userCardData = UserCardEndPointsHandler.getUserCardOnDate(anotherUser.getUserId(), LocalDate.now());

        //then
        assertThat(userCardData, notNullValue());
        assertThat(userCardData.getCupsToShow(), notNullValue());
        assertThat(userCardData.getCupsToShow(), hasSize(0));
    }

    @Test
    public void shouldGetAnotherUserCardData() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        CupEditDTO cup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), pcs.getPcsId())
                .future()
                .publicCup()
                .build());
        MatchEditDTO match = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId())
                .future()
                .build()
        );

        UserDTO anotherUser = AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category.getCategoryId());
        BetEndPointsHandler.make(match.getMatchId(), 1, 2);

        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        // when
        UserCardDTO userCardData = UserCardEndPointsHandler.getUserCardOnDate(anotherUser.getUserId(), ON_DATE);

        //then
        assertThat(userCardData.getCupsToShow(), hasSize(1));
        ComparisonUtils.assertTheSame(cup, userCardData.getCupsToShow().get(0));
    }
}
