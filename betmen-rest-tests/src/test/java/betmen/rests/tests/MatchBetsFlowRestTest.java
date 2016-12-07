package betmen.rests.tests;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.rests.common.ErrorCodes;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.google.common.collect.Lists;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class MatchBetsFlowRestTest {

    private static final Logger LOGGER = Logger.getLogger(AuthEndPointsHandler.class);

    private static final int BUSINESS_EXCEPTION = ResponseStatus.BUSINESS_EXCEPTION.getCode();
    private static final int UNPROCESSABLE_ENTITY = ResponseStatus.UNPROCESSABLE_ENTITY.getCode();
    private static final int UNAUTHORIZED = ResponseStatus.UNAUTHORIZED.getCode();

    UserRegData userData;
    UserDTO user;

    UserRegData anotherUserData;
    UserDTO anotherUser;

    private CategoryEditDTO category;
    private CategoryEditDTO anotherCategory;

    private CupEditDTO cup;

    private TeamEditDTO cupWinner1;
    private TeamEditDTO cupWinner2;
    private TeamEditDTO cupTeam1;
    private TeamEditDTO cupTeam2;

    private MatchEditDTO match;

    private CupEditDTO anotherCup;
    private MatchEditDTO anotherMatch;

    private TeamEditDTO anotherCupTeam1;
    private TeamEditDTO anotherCupTeam2;

    @BeforeClass
    public void sauteInit() {
        DataCleanUpUtils.cleanupAll();

        userData = RandomUtils.randomUser();
        LOGGER.debug(String.format("USER_DATA: %s / %s", userData.getLogin(), userData.getPassword()));
        user = AuthEndPointsHandler.registerNewUserAndLogin(userData);

        anotherUserData = RandomUtils.randomUser();
        LOGGER.debug(String.format("ANOTHER_USER_DATA: %s / %s", anotherUserData.getLogin(), anotherUserData.getPassword()));
        anotherUser = AuthEndPointsHandler.registerNewUserAndLogin(anotherUserData);

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
        category = AdminTestDataGenerator.createCategory();
        anotherCategory = AdminTestDataGenerator.createCategory();

        cup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId()).publicCup().build());
        cupWinner1 = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());
        cupWinner2 = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());
        cupTeam1 = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());
        cupTeam2 = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());
        match = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup.getCupId(), cupTeam1.getTeamId(), cupTeam2.getTeamId()).future().build());

        anotherCup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId()).publicCup().build());
        anotherCupTeam1 = AdminTestDataGenerator.createTeamAndActivateForCup(anotherCup.getCategoryId(), anotherCup.getCupId());
        anotherCupTeam2 = AdminTestDataGenerator.createTeamAndActivateForCup(anotherCup.getCategoryId(), anotherCup.getCupId());
        anotherMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(anotherCup.getCupId(), anotherCupTeam1.getTeamId(), anotherCupTeam2.getTeamId()).future().build());

        AuthEndPointsHandler.logout();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test(priority = 10)
    public void shouldNotGetBetForAnonymous() {
        UserDTO user = AuthEndPointsHandler.registerNewUser();
        BetEndPointsHandler.get(match.getMatchId(), user.getUserId(), UNAUTHORIZED);
    }

    @Test(priority = 20)
    public void shouldNotMakeBetForAnonymous() {
        BetEndPointsHandler.make(match.getMatchId(), 0, 2, ResponseStatus.UNAUTHORIZED);
    }

    @Test(priority = 30)
    public void shouldGetMatchWithNullBetIfBetDoesNotExist() {
        loginAsUser();
        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet, notNullValue());
        ComparisonUtils.assertTheSame(matchBet.getMatch(), match);
        assertThat(matchBet.getBet(), nullValue());
        assertThat(matchBet.isBettingAllowed(), is(true));
        assertThat(matchBet.getBettingValidationMessage(), is(StringUtils.EMPTY));
        assertThat(matchBet.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet.getTotalBets(), is(0));
    }

    @Test(priority = 40)
    public void shouldReturnBetOfCurrentUserIfUserIdIsNotProvided() {
        loginAsUser();
        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), 0);
        assertThat(matchBet, notNullValue());
        ComparisonUtils.assertTheSame(matchBet.getMatch(), match);
        assertThat(matchBet.getBet(), nullValue());
    }

    @Test(priority = 50)
    public void shouldFailIfUserIdIsWrong() {
        loginAsUser();
        BetEndPointsHandler.get(match.getMatchId(), 66767, UNPROCESSABLE_ENTITY);
    }

    @Test(priority = 60)
    public void anotherUserCanSeeBetAbsentOfUserIfMatchHasNotStartedYet() {
        loginAsAnotherUser();
        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet, notNullValue());
        ComparisonUtils.assertTheSame(matchBet.getMatch(), match);
        assertThat(matchBet.getBet(), nullValue());
        assertThat(matchBet.getTotalBets(), is(0));
        assertThat(matchBet.isBettingAllowed(), is(true));
        assertThat(matchBet.getBettingValidationMessage(), is(StringUtils.EMPTY));
        assertThat(matchBet.getUserMatchPointsHolder(), nullValue());
    }

    @Test(priority = 70)
    public void userCanMakeBet() {
        loginAsUser();
        BetDTO bet = BetEndPointsHandler.make(match.getMatchId(), 1, 3);
        assertThat(bet, notNullValue());
        assertThat(bet.getMatch(), notNullValue());
        ComparisonUtils.assertTheSame(bet.getMatch(), match);
        assertThat(bet.getScore1(), is(1));
        assertThat(bet.getScore2(), is(3));
        assertThat(bet.isSecuredBet(), is(false));
    }

    @Test(priority = 80)
    public void userCanUpdateBet() {
        loginAsUser();
        BetDTO bet = BetEndPointsHandler.make(match.getMatchId(), 2, 2);
        assertThat(bet, notNullValue());
        assertThat(bet.getMatch(), notNullValue());
        ComparisonUtils.assertTheSame(bet.getMatch(), match);
        assertThat(bet.getScore1(), is(2));
        assertThat(bet.getScore2(), is(2));
        assertThat(bet.isSecuredBet(), is(false));

        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet.getTotalBets(), is(1));
    }

    @Test(priority = 90)
    public void anotherUserSeesBetOfUserAsSecuredIfMatchHasNotStartedYet() {
        loginAsAnotherUser();
        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet, notNullValue());
        ComparisonUtils.assertTheSame(matchBet.getMatch(), match);
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().isSecuredBet(), is(true));
        assertThat(matchBet.getBet().getScore1(), is(0));
        assertThat(matchBet.getBet().getScore2(), is(0));
        assertThat(matchBet.getTotalBets(), is(1));
        assertThat(matchBet.isBettingAllowed(), is(true));
        assertThat(matchBet.getBettingValidationMessage(), is(StringUtils.EMPTY));
        assertThat(matchBet.getUserMatchPointsHolder(), notNullValue());
    }

    @Test(priority = 100)
    public void userCanDeleteOwnBet() {
        loginAsUser();
        assertThat(BetEndPointsHandler.delete(match.getMatchId()), is(true));
        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet, notNullValue());
        assertThat(matchBet.getBet(), nullValue());
    }

    @Test(priority = 110)
    public void userCreateNewBet() {
        loginAsUser();
        BetEndPointsHandler.make(match.getMatchId(), 4, 5);
        BetEndPointsHandler.make(anotherMatch.getMatchId(), 14, 25);
    }

    @Test(priority = 120)
    public void matchHasStarted() {
        AuthEndPointsHandler.loginAsAdmin();
        match.setBeginningTime(LocalDateTime.now().minusMinutes(30));
        match.setScore1(1);
        match.setScore2(0);
        AdminMatchEndPointsHandler.update(match);
        AuthEndPointsHandler.logout();
    }

    @Test(priority = 130)
    public void userCanNotChangeHisBetAfterMatchStart() {
        loginAsUser();
        Response response = BetEndPointsHandler.make(match.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_HAS_ALREADY_STARTED), is(true));
    }

    @Test(priority = 140)
    public void userCanNotDeleteHisBetAfterMatchStart() {
        loginAsUser();
        Response response = BetEndPointsHandler.delete(match.getMatchId(), BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_HAS_ALREADY_STARTED), is(true));
    }

    @Test(priority = 150)
    public void anotherUserCanNotCreateNewBetAfterMatchStarted() {
        loginAsAnotherUser();
        Response response = BetEndPointsHandler.make(match.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_HAS_ALREADY_STARTED), is(true));
    }

    @Test(priority = 220)
    public void adminFinishMatch() {
        AuthEndPointsHandler.loginAsAdmin();
        finishMatch(match);
        AuthEndPointsHandler.logout();
    }

    @Test(priority = 230)
    public void userCanNotChangeHisBetAfterMatchIsClosed() {
        loginAsUser();
        Response response = BetEndPointsHandler.make(match.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_FINISHED), is(true));
    }

    @Test(priority = 240)
    public void userCanNotDeleteHisBetAfterMatchIsClosed() {
        loginAsUser();
        Response response = BetEndPointsHandler.delete(match.getMatchId(), BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_FINISHED), is(true));
    }

    @Test(priority = 250)
    public void anotherUserCanNotCreateNewBetAfterMatchIsClosed() {
        loginAsAnotherUser();
        Response response = BetEndPointsHandler.make(match.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_FINISHED), is(true));
    }

    /*@Test(priority = 260)
    public void adminCloseMatchBettingForAnotherCup() {
        AuthEndPointsHandler.loginAsAdmin();
        anotherCup.setReadyForMatchBets(false);
        AdminCupEndPointsHandler.update(anotherCup);
        AuthEndPointsHandler.logout();
    }

    @Test(priority = 270)
    public void userCanNotChangeHisBetAfterCupClosingForMatchBetting() {
        loginAsUser();
        Response response = BetEndPointsHandler.make(anotherMatch.getMatchId(), 1, 1, BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_BETTING_FINISHED), is(true));
    }

    @Test(priority = 280)
    public void userCanNotDeleteHisBetAfterCupClosingForMatchBetting() {
        loginAsUser();
        Response response = BetEndPointsHandler.delete(anotherMatch.getMatchId(), BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_BETTING_FINISHED), is(true));
    }

    @Test(priority = 290)
    public void anotherUserCanNotCreateNewBetAfterCupClosingForMatchBetting() {
        loginAsAnotherUser();
        Response response = BetEndPointsHandler.make(anotherMatch.getMatchId(), 1, 1, BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.MATCH_BETTING_FINISHED), is(true));
    }*/

    @Test(priority = 360)
    public void adminCloseAnotherCup() {
        AuthEndPointsHandler.loginAsAdmin();
        finishCup(anotherCup);
        AuthEndPointsHandler.logout();
    }

    @Test(priority = 370)
    public void userCanNotChangeHisBetAfterCupIsClosedEvenIfMatchHasNotFinished() {
        loginAsUser();
        Response response = BetEndPointsHandler.make(anotherMatch.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.CUP_FINISHED), is(true));
    }

    @Test(priority = 380)
    public void userCanNotDeleteHisBetAfterCupIsClosedEvenIfMatchHasNotFinished() {
        loginAsUser();
        Response response = BetEndPointsHandler.delete(anotherMatch.getMatchId(), BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.CUP_FINISHED), is(true));
    }

    @Test(priority = 390)
    public void anotherUserCanNotCreateNewBetAfterCupIsClosedEvenIfMatchHasNotFinished() {
        loginAsAnotherUser();
        Response response = BetEndPointsHandler.make(anotherMatch.getMatchId(), 1, 1, ResponseStatus.BUSINESS_EXCEPTION);
        CommonErrorResponse errorsResponse = response.as(CommonErrorResponse.class);
        assertThat(errorsResponse.containsError(ErrorCodes.CUP_FINISHED), is(true));
    }


    @Test(enabled = false)
    public void shouldGoThroughFlow() {

        // user makes a bet
        UserRegData userData = RandomUtils.randomUser();
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin(userData);

        // no bet yet
        MatchBetDTO matchBet_1 = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(matchBet_1, notNullValue());
        assertThat(matchBet_1.getBet(), nullValue()); // no bet

        // make a bet and check it exists
        BetDTO bet = BetEndPointsHandler.make(match.getMatchId(), 4, 1); // can make
        MatchBetDTO matchBet_2 = BetEndPointsHandler.get(match.getMatchId(), user.getUserId()); // can get
        assertThat(matchBet_2, notNullValue());
        assertThat(matchBet_2.getBet(), notNullValue()); // bet exists
        assertThat(matchBet_2.getBet().getScore1(), is(4));
        assertThat(matchBet_2.getBet().getScore2(), is(1));

        // delete bet and check it does not exist
        assertThat(BetEndPointsHandler.delete(match.getMatchId()), is(true)); // can delete
        MatchBetDTO match_no_bet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId());
        assertThat(match_no_bet, notNullValue());
        assertThat(match_no_bet.getBet(), nullValue());

        BetEndPointsHandler.make(match.getMatchId(), 6, 3); // can make
        MatchBetDTO matchBet0 = BetEndPointsHandler.get(match.getMatchId(), user.getUserId()); // can get
        assertThat(matchBet0, notNullValue());
        assertThat(matchBet0.getBet(), notNullValue()); // bet exists
        assertThat(matchBet0.getBet().getScore1(), is(6));
        assertThat(matchBet0.getBet().getScore2(), is(3));
        AuthEndPointsHandler.logout();

        // another user makes a bet
        UserRegData anotherUserData = RandomUtils.randomUser();
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUserAndLogin(anotherUserData);
        BetDTO anotherBet = BetEndPointsHandler.make(match.getMatchId(), 2, 3);
        BetEndPointsHandler.delete(match.getMatchId(), UNPROCESSABLE_ENTITY); // cannot delete bet of first user
        AuthEndPointsHandler.logout();

        // finish match amd cup
        AuthEndPointsHandler.loginAsAdmin();
        finishMatch(match);
        finishCup(cup);
        AuthEndPointsHandler.logout();

        // login as user - he cannot change his bet but can read own bet and bets of another user
        login(userData);

        BetEndPointsHandler.make(match.getMatchId(), 0, 0, ResponseStatus.UNPROCESSABLE_ENTITY); // cannot make bet

        MatchBetDTO matchBet = BetEndPointsHandler.get(match.getMatchId(), user.getUserId()); // can get
        assertThat(matchBet, notNullValue());
        assertThat(matchBet.getBet(), notNullValue());

        BetEndPointsHandler.delete(match.getMatchId(), UNPROCESSABLE_ENTITY); // cannot delete bet
        BetEndPointsHandler.delete(match.getMatchId(), UNPROCESSABLE_ENTITY); // cannot delete bet of another user
        MatchBetDTO matchBet1 = BetEndPointsHandler.get(match.getMatchId(), user.getUserId()); // can get
        assertThat(matchBet1, notNullValue());
        assertThat(matchBet1.getBet(), notNullValue()); // bet still exists

        assertThat(BetEndPointsHandler.get(match.getMatchId(), anotherUser.getUserId()), notNullValue()); // can get bet of another user

        AuthEndPointsHandler.logout();
    }

    private void finishCup(final CupEditDTO cup) {
        cup.setWinnersCount(1);
        cup.setCupWinners(Lists.newArrayList(convertToWinner(1, cupWinner1.getTeamId())));
        AdminCupEndPointsHandler.update(cup);
    }

    private void finishMatch(final MatchEditDTO match) {
        match.setScore1(1);
        match.setScore2(5);
        match.setMatchFinished(true);
        AdminMatchEndPointsHandler.update(match);
    }

    private CupWinnerEditDTO convertToWinner(final int position, final int teamId) {
        CupWinnerEditDTO cupWinner = new CupWinnerEditDTO();
        cupWinner.setCupPosition(position);
        cupWinner.setTeamId(teamId);
        return cupWinner;
    }

    private void loginAsUser() {
        login(userData);
    }

    private void loginAsAnotherUser() {
        login(anotherUserData);
    }

    private void login(final UserRegData regData) {
        AuthEndPointsHandler.login(regData);
    }
}
