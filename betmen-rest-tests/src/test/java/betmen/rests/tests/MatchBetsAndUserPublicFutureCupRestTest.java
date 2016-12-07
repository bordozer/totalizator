package betmen.rests.tests;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class MatchBetsAndUserPublicFutureCupRestTest {

    private int matchId;

    @BeforeMethod
    public void sauteInit() {
        AuthEndPointsHandler.loginAsAdmin();

        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
        CupEditDTO constructedCup = CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId()).publicCup().future().build();
        int cupId = AdminCupEndPointsHandler.create(constructedCup).getCupId();
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        MatchEditDTO match = MatchTemplater.random(cupId, team1.getTeamId(), team2.getTeamId()).future().build();
        matchId = AdminMatchEndPointsHandler.create(match).getMatchId();

        AuthEndPointsHandler.logout();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldSaveBetForRegistered() {
        int score1 = 76;
        int score2 = 78;

        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        BetDTO bet = BetEndPointsHandler.make(matchId, score1, score2);
        assertThat(bet, notNullValue());
        assertThat(bet.getUser(), notNullValue());
        assertThat(bet.getMatch(), notNullValue());
        assertThat(bet.getScore1(), is(score1));
        assertThat(bet.getScore2(), is(score2));
        assertThat(bet.isSecuredBet(), is(false)); // user can see own bet
        ComparisonUtils.assertEqual(bet.getUser(), user);

        MatchBetDTO matchBet = BetEndPointsHandler.get(matchId, user.getUserId());
        assertThat(matchBet, notNullValue());
        assertThat(matchBet.isBettingAllowed(), is(true));
        assertThat(matchBet.getBettingValidationMessage(), is(""));
        assertThat(matchBet.getTotalBets(), is(1));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().isSecuredBet(), is(false));  // user can see own bet
        ComparisonUtils.assertEqual(matchBet.getMatch(), bet.getMatch());
        ComparisonUtils.assertEqual(matchBet.getBet(), bet);

        // update bet
        BetEndPointsHandler.make(matchId, 77, 55);
        MatchBetDTO updatedBatchBet = BetEndPointsHandler.get(matchId, user.getUserId());
        assertThat(updatedBatchBet, notNullValue());
        assertThat(updatedBatchBet.getBet(), notNullValue());
        assertThat(updatedBatchBet.getBet().getScore1(), is(77));
        assertThat(updatedBatchBet.getBet().getScore2(), is(55));
    }

    @Test
    public void shouldNotSeeBetDetailsIfMatchNotStarted() {
        int score1 = 76;
        int score2 = 78;

        // user i make a bet
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();
        BetEndPointsHandler.make(matchId, score1, score2);
        AuthEndPointsHandler.logout();

        // user 2 has to see a secure bet
        AuthEndPointsHandler.registerNewUserAndLogin();
        MatchBetDTO theSameMatchBet = BetEndPointsHandler.get(matchId, user.getUserId());
        assertThat(theSameMatchBet, notNullValue());
        assertThat(theSameMatchBet.isBettingAllowed(), is(true));
        assertThat(theSameMatchBet.getBettingValidationMessage(), is(""));
        assertThat(theSameMatchBet.getTotalBets(), is(1));
        assertThat(theSameMatchBet.getBet(), notNullValue());
        assertThat(theSameMatchBet.getBet().isSecuredBet(), is(true));  // user can see bet of not started match
        assertThat(theSameMatchBet.getBet().getScore1(), is(0)); // user can see bet of not started match
        assertThat(theSameMatchBet.getBet().getScore2(), is(0)); // user can see bet of not started match
    }

    @Test
    public void shouldNotAllowNegativeBetPoints() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        Response response = BetEndPointsHandler.make(matchId, -1, 0, ResponseStatus.BAD_REQUEST);
        CommonErrorResponse errorResponse = response.as(CommonErrorResponse.class);
        assertThat(errorResponse.containsError("errors.points_cannot_be_negative"), is(true));
    }
}
