package betmen.rests.tests;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.MatchBetsOnDateDTO;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.MatchEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import net.jcip.annotations.NotThreadSafe;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

@NotThreadSafe
public class MnBWidgetFlowRestTest {

    private static final int ASC_SORTING = 1;

    private static final int PROROK_BET_SCORE_1 = 2;
    private static final int PROROK_BET_SCORE_2 = 3;

    private static final int LOOSER_BET_SCORE_1 = 4;
    private static final int LOOSER_BET_SCORE_2 = 3;

    private static final int LUCKY_USER_BET_SCORE_1 = 1;
    private static final int LUCKY_USER_BET_SCORE_2 = 3;

    private static final int MATCH_SCORE_1 = PROROK_BET_SCORE_1;
    private static final int MATCH_SCORE_2 = PROROK_BET_SCORE_2;

    private UserRegData prorokRegData;
    private UserDTO prorokUser;

    private UserRegData looserUserRegData;
    private UserDTO looserUser;

    private UserRegData luckyUserRegData;
    private UserDTO luckyUser;

    private CupEditDTO cup1;
    private TeamEditDTO team11;
    private TeamEditDTO team12;
    private TeamEditDTO team13;
    private TeamEditDTO team14;
    private MatchEditDTO cup1EarlyMatch;
    private MatchEditDTO cup1LateMatch;

    private CupEditDTO cup2;
    private TeamEditDTO team21;
    private TeamEditDTO team22;
    private TeamEditDTO team23;
    private TeamEditDTO team24;
    private TeamEditDTO team25;
    private MatchEditDTO cup2LateMatch;
    private MatchEditDTO cup2MiddleMatch;
    private MatchEditDTO cup2EarlyMatch;

    @BeforeClass
    public void sauteInit() {
        DataCleanUpUtils.cleanupAll();

        prorokRegData = RandomUtils.randomUser();
        prorokUser = AuthEndPointsHandler.registerNewUserAndLogin(prorokRegData);

        looserUserRegData = RandomUtils.randomUser();
        looserUser = AuthEndPointsHandler.registerNewUserAndLogin(looserUserRegData);

        luckyUserRegData = RandomUtils.randomUser();
        luckyUser = AuthEndPointsHandler.registerNewUserAndLogin(luckyUserRegData);

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();

        cup1 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pointsStrategy.getPcsId());
        team11 = AdminTestDataGenerator.createTeamAndActivateForCup(cup1.getCategoryId(), cup1.getCupId());
        team12 = AdminTestDataGenerator.createTeamAndActivateForCup(cup1.getCategoryId(), cup1.getCupId());
        team13 = AdminTestDataGenerator.createTeamAndActivateForCup(cup1.getCategoryId(), cup1.getCupId());
        team14 = AdminTestDataGenerator.createTeamAndActivateForCup(cup1.getCategoryId(), cup1.getCupId());
        cup1EarlyMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team11.getTeamId(), team12.getTeamId()).future().withBeginningTime(LocalDateTime.now().plusMinutes(3)).build());
        cup1LateMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup1.getCupId(), team13.getTeamId(), team14.getTeamId()).future().withBeginningTime(LocalDateTime.now().plusMinutes(5)).build());

        cup2 = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pointsStrategy.getPcsId());
        team21 = AdminTestDataGenerator.createTeamAndActivateForCup(cup2.getCategoryId(), cup2.getCupId());
        team22 = AdminTestDataGenerator.createTeamAndActivateForCup(cup2.getCategoryId(), cup2.getCupId());
        team23 = AdminTestDataGenerator.createTeamAndActivateForCup(cup2.getCategoryId(), cup2.getCupId());
        team24 = AdminTestDataGenerator.createTeamAndActivateForCup(cup2.getCategoryId(), cup2.getCupId());
        team25 = AdminTestDataGenerator.createTeamAndActivateForCup(cup2.getCategoryId(), cup2.getCupId());

        cup2EarlyMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team23.getTeamId(), team24.getTeamId()).future().build());
        cup2MiddleMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team21.getTeamId(), team25.getTeamId()).future().withBeginningTime(LocalDateTime.now().plusDays(3)).build());
        cup2LateMatch = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup2.getCupId(), team21.getTeamId(), team22.getTeamId()).future().withBeginningTime(LocalDateTime.now().plusDays(4)).build());

        AuthEndPointsHandler.logout();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldFailIfUserAnonymous() {
        MatchSearchModelDto model = new MatchSearchModelDto();
        MatchEndPointsHandler.searchMatches(model, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldFailIfCupIdNotProvided() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        MatchSearchModelDto model = new MatchSearchModelDto();
        FieldErrorsResponse errorResponse = MatchEndPointsHandler.searchMatches(model, ResponseStatus.BAD_REQUEST).as(FieldErrorsResponse.class);
        assertThat(errorResponse.errorsCount(), is(1));
        assertThat(errorResponse.containsError("cupId", "must be greater than or equal to 1"), is(true));
    }

    @Test
    public void shouldFailIfWrongCupId() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(102030);
        searchModel.setShowFinished(true);
        searchModel.setShowFutureMatches(true);
        List<MatchBetsOnDateDTO> dtos = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(dtos, hasSize(0));
    }

    @Test
    public void shouldFailIfFutureAndFinishedMatchesFlagsAreNotCheckedAtTheSameTime() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(102030);
        MatchEndPointsHandler.searchMatches(searchModel, ResponseStatus.BAD_REQUEST);
    }

    @Test(priority = 10)
    public void shouldReturnAllOpenMatchesOfCup1() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(1));

        List<MatchBetDTO> dateMatches = searchResult.get(0).getMatchBets();
        assertThat(dateMatches, notNullValue());
        assertThat(dateMatches.size(), is(2));

        MatchBetDTO matchBet1 = dateMatches.get(0);
        assertThat(matchBet1, notNullValue());
        ComparisonUtils.assertTheSame(matchBet1.getMatch(), cup1LateMatch);
        assertThat(matchBet1.getMatchId(), is(cup1LateMatch.getMatchId()));
        assertThat(matchBet1.getTotalBets(), is(0));
        assertThat(matchBet1.getBettingValidationMessage(), is(""));
        assertThat(matchBet1.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet1.getBet(), nullValue());

        MatchBetDTO matchBet2 = dateMatches.get(1);
        assertThat(matchBet2, notNullValue());
        ComparisonUtils.assertTheSame(matchBet2.getMatch(), cup1EarlyMatch);
        assertThat(matchBet2.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet2.getTotalBets(), is(0));
        assertThat(matchBet2.getBettingValidationMessage(), is(""));
        assertThat(matchBet2.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet2.getBet(), nullValue());
    }

    @Test(priority = 10)
    public void shouldReturnAllOpenMatchesOfCup2() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        // sorting: 1 is ASC, all remains are DESC, default int is 0 so it will nbe DESC sorting by default

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(3));

        List<MatchBetDTO> matchesOnDate1 = searchResult.get(0).getMatchBets();
        assertThat(matchesOnDate1, notNullValue());
        assertThat(matchesOnDate1.size(), is(1));

        MatchBetDTO matchBet1 = matchesOnDate1.get(0);
        assertThat(matchBet1, notNullValue());
        ComparisonUtils.assertTheSame(matchBet1.getMatch(), cup2LateMatch);
        assertThat(matchBet1.getMatchId(), is(cup2LateMatch.getMatchId()));
        assertThat(matchBet1.getTotalBets(), is(0));
        assertThat(matchBet1.getBettingValidationMessage(), is(""));
        assertThat(matchBet1.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet1.getBet(), nullValue());

        List<MatchBetDTO> matchesOnDate2 = searchResult.get(1).getMatchBets();
        assertThat(matchesOnDate2, notNullValue());
        assertThat(matchesOnDate2.size(), is(1));

        MatchBetDTO matchBet2 = matchesOnDate2.get(0);
        assertThat(matchBet2, notNullValue());
        ComparisonUtils.assertTheSame(matchBet2.getMatch(), cup2MiddleMatch);
        assertThat(matchBet2.getMatchId(), is(cup2MiddleMatch.getMatchId()));
        assertThat(matchBet2.getTotalBets(), is(0));
        assertThat(matchBet2.getBettingValidationMessage(), is(""));
        assertThat(matchBet2.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet2.getBet(), nullValue());

        List<MatchBetDTO> matchesOnDate3 = searchResult.get(2).getMatchBets();
        assertThat(matchesOnDate3, notNullValue());
        assertThat(matchesOnDate3.size(), is(1));

        MatchBetDTO matchBet3 = matchesOnDate3.get(0);
        assertThat(matchBet3, notNullValue());
        ComparisonUtils.assertTheSame(matchBet3.getMatch(), cup2EarlyMatch);
        assertThat(matchBet3.getMatchId(), is(cup2EarlyMatch.getMatchId()));
        assertThat(matchBet3.getTotalBets(), is(0));
        assertThat(matchBet3.getBettingValidationMessage(), is(""));
        assertThat(matchBet3.getUserMatchPointsHolder(), nullValue());
        assertThat(matchBet3.getBet(), nullValue());
    }

    @Test(priority = 10)
    public void shouldRespectSorting() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setSorting(ASC_SORTING);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(3));

        List<MatchBetDTO> matchesOnDate1 = searchResult.get(0).getMatchBets();
        MatchBetDTO matchBet1 = matchesOnDate1.get(0);
        assertThat(matchBet1.getMatchId(), is(cup2EarlyMatch.getMatchId()));

        List<MatchBetDTO> matchesOnDate2 = searchResult.get(1).getMatchBets();
        MatchBetDTO matchBet2 = matchesOnDate2.get(0);
        assertThat(matchBet2.getMatchId(), is(cup2MiddleMatch.getMatchId()));

        List<MatchBetDTO> matchesOnDate3 = searchResult.get(2).getMatchBets();
        MatchBetDTO matchBet3 = matchesOnDate3.get(0);
        assertThat(matchBet3.getMatchId(), is(cup2LateMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldRespectFilteringByTeam1() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setTeamId(team21.getTeamId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(2));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup2LateMatch.getMatchId()));
        assertThat(searchResult.get(1).getMatchBets().get(0).getMatchId(), is(cup2MiddleMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldRespectFilteringByTeam2() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setTeam2Id(team21.getTeamId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(2));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup2LateMatch.getMatchId()));
        assertThat(searchResult.get(1).getMatchBets().get(0).getMatchId(), is(cup2MiddleMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldRespectFilteringByTeam1And2() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setTeamId(team22.getTeamId());
        searchModel.setTeam2Id(team21.getTeamId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(1));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup2LateMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldRespectFilteringByTeam2And1() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setTeamId(team21.getTeamId());
        searchModel.setTeam2Id(team22.getTeamId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(1));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup2LateMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldReturnEmptyResultIfNoFinishedMatchesYet() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFinished(true);

        assertThat(MatchEndPointsHandler.searchMatches(searchModel).size(), is(0));
    }

    @Test(priority = 10)
    public void shouldReturnAllMatches() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setShowFinished(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(3));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup2LateMatch.getMatchId()));
        assertThat(searchResult.get(1).getMatchBets().get(0).getMatchId(), is(cup2MiddleMatch.getMatchId()));
        assertThat(searchResult.get(2).getMatchBets().get(0).getMatchId(), is(cup2EarlyMatch.getMatchId()));
    }

    @Test(priority = 10)
    public void shouldFailSearchFilterByDateRequestedWithoutDate() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup2.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setFilterByDateEnable(true);

        MatchEndPointsHandler.searchMatches(searchModel, ResponseStatus.BAD_REQUEST);
    }

    @Test(priority = 10)
    public void shouldFilterByDate() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setFilterByDateEnable(true);
        searchModel.setFilterByDate(DateTimeUtils.formatDate(cup1EarlyMatch.getBeginningTime()));

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult.size(), is(1));
        assertThat(searchResult.get(0).getMatchBets().size(), is(2));

        assertThat(searchResult.get(0).getMatchBets().get(0).getMatchId(), is(cup1LateMatch.getMatchId()));
        assertThat(searchResult.get(0).getMatchBets().get(1).getMatchId(), is(cup1EarlyMatch.getMatchId()));
    }

    @Test(priority = 20)
    public void userMakeBetAndCanSeeItOneInSearchResultByDefaultWithoutFilterByUser() {
        AuthEndPointsHandler.login(prorokRegData);
        BetDTO bet = BetEndPointsHandler.make(cup1EarlyMatch.getMatchId(), PROROK_BET_SCORE_1, PROROK_BET_SCORE_2);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setFilterByDateEnable(true);
        searchModel.setFilterByDate(DateTimeUtils.formatDate(cup1EarlyMatch.getBeginningTime()));

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);

        // cup1LateMatch does not have prorokUser's bet
        MatchBetDTO matchBet1 = searchResult.get(0).getMatchBets().get(0);
        assertThat(matchBet1.getMatchId(), is(cup1LateMatch.getMatchId()));
        assertThat(matchBet1.getBet(), nullValue());
        assertThat(matchBet1.getUserMatchPointsHolder(), nullValue());

        // cup1EarlyMatch has prorokUser's bet
        MatchBetDTO matchBet2 = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet2.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet2.getBet(), notNullValue());
        ComparisonUtils.assertTheSame(matchBet2.getBet().getMatch(), cup1EarlyMatch);
        assertThat(matchBet2.getBet().getScore1(), is(PROROK_BET_SCORE_1));
        assertThat(matchBet2.getBet().getScore2(), is(PROROK_BET_SCORE_2));
        assertThat(matchBet2.getBet().isSecuredBet(), is(false));
        assertEmptyPointsHolder(matchBet2, prorokUser);
        ComparisonUtils.assertEqual(matchBet2.getBet().getUser(), prorokUser);
    }

    @Test(priority = 30)
    public void newUserWithoutBetSeeNoBetInSearchResultWithoutFilterByUser() {
        UserDTO newUser = AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);

        // by default looserUser sees own bet - so no looserUser's bet yet and no bet in found match
        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), nullValue());
    }

    @Test(priority = 30)
    public void newUserWithoutBetSeeBetOfUserInSearchResultWithFilterByUser() {
        UserDTO newUser = AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setUserId(prorokUser.getUserId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);

        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(0));
        assertThat(matchBet.getBet().getScore2(), is(0));
        assertThat(matchBet.getBet().isSecuredBet(), is(true));
        assertEmptyPointsHolder(matchBet, prorokUser);
        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), prorokUser);
    }

    @Test(priority = 40)
    public void newUserWithBetSeesBetOfUserInSearchResultWithFilterByUser() {
        AuthEndPointsHandler.login(looserUserRegData);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setUserId(prorokUser.getUserId());

        // looserUser makes his own bet
        BetDTO bet = BetEndPointsHandler.make(cup1EarlyMatch.getMatchId(), LOOSER_BET_SCORE_1, LOOSER_BET_SCORE_2);

        // ... but search parameters a the same (with prorokUser.id)
        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(0));
        assertThat(matchBet.getBet().getScore2(), is(0));
        assertThat(matchBet.getBet().isSecuredBet(), is(true));
        assertEmptyPointsHolder(matchBet, prorokUser);
        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), prorokUser);
    }

    @Test(priority = 40)
    public void newUserWithBetSeesOwnBetInSearchResultWithFilterByOwnId() {
        AuthEndPointsHandler.login(luckyUserRegData);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setUserId(luckyUser.getUserId());

        // newUser makes his own bet
        BetDTO bet = BetEndPointsHandler.make(cup1EarlyMatch.getMatchId(), LUCKY_USER_BET_SCORE_1, LUCKY_USER_BET_SCORE_2);

        // search parameters a the same (with prorokUser.id)
        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(LUCKY_USER_BET_SCORE_1));
        assertThat(matchBet.getBet().getScore2(), is(LUCKY_USER_BET_SCORE_2));
        assertThat(matchBet.getBet().isSecuredBet(), is(false));
        assertEmptyPointsHolder(matchBet, looserUser);
        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), prorokUser);
    }

    @Test(priority = 100)
    public void matchHasStarted() {
        AuthEndPointsHandler.loginAsAdmin();
        cup1EarlyMatch.setBeginningTime(LocalDateTime.now().minusMinutes(1));
        cup1EarlyMatch = AdminMatchEndPointsHandler.update(this.cup1EarlyMatch);
    }

    @Test(priority = 110)
    public void newUserSeesNonSecureBetOfUserInSearchResultWithFilterByUserAfterMatchStarted() {
        UserDTO newUser = AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setUserId(prorokUser.getUserId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(1);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(PROROK_BET_SCORE_1));
        assertThat(matchBet.getBet().getScore2(), is(PROROK_BET_SCORE_2));
        assertThat(matchBet.getBet().isSecuredBet(), is(false));
        assertEmptyPointsHolder(matchBet, prorokUser);
        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), prorokUser);
    }

    @Test(priority = 200)
    public void matchHasFinished() {
        AuthEndPointsHandler.loginAsAdmin();

        cup1EarlyMatch.setBeginningTime(LocalDateTime.now().minusHours(3));
        cup1EarlyMatch.setMatchFinished(true);
        cup1EarlyMatch.setScore1(MATCH_SCORE_1);
        cup1EarlyMatch.setScore2(MATCH_SCORE_2);

        cup1EarlyMatch = AdminMatchEndPointsHandler.update(this.cup1EarlyMatch);
    }

    @Test(priority = 210)
    public void finishedMatchIsNotSeenByFutureOptionAnyMore() {
        AuthEndPointsHandler.login(prorokRegData);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFutureMatches(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(1));
        ComparisonUtils.assertTheSame(searchResult.get(0).getMatchBets().get(0).getMatch(), cup1LateMatch);
    }

    @Test(priority = 210)
    public void newUserSeesNonSecureBetOfUserAndPointsOfUserAfterMatchFinished() {
        UserDTO newUser = AuthEndPointsHandler.registerNewUserAndLogin();

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFinished(true);
        searchModel.setUserId(prorokUser.getUserId());

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(1));

        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(0);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(PROROK_BET_SCORE_1));
        assertThat(matchBet.getBet().getScore2(), is(PROROK_BET_SCORE_2));
        assertThat(matchBet.getBet().isSecuredBet(), is(false));

        UserMatchPointsHolderDTO holder = matchBet.getUserMatchPointsHolder();
        assertThat(holder, notNullValue());
        assertThat(holder.getMatchBetPoints(), is("6"));
        assertThat(holder.getMatchBonus(), is("0.50"));
        assertThat(holder.getSummary(), is(6.50f));
        assertThat(holder.getSummaryPoints(), is("6.50"));
        ComparisonUtils.assertEqual(holder.getUser(), prorokUser);

        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), prorokUser);
    }

    @Test(priority = 210)
    public void looserUserSeesOwnBetAndPointsAfterMatchFinished() {
        AuthEndPointsHandler.login(looserUserRegData);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFinished(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(1));

        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(0);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(LOOSER_BET_SCORE_1));
        assertThat(matchBet.getBet().getScore2(), is(LOOSER_BET_SCORE_2));
        assertThat(matchBet.getBet().isSecuredBet(), is(false));

        UserMatchPointsHolderDTO holder = matchBet.getUserMatchPointsHolder();
        assertThat(holder, notNullValue());
        assertThat(holder.getMatchBetPoints(), is("-1"));
        assertThat(holder.getMatchBonus(), is("0.00"));
        assertThat(holder.getSummary(), is(-1.00f));
        assertThat(holder.getSummaryPoints(), is("-1.00"));
        ComparisonUtils.assertEqual(holder.getUser(), looserUser);

        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), looserUser);
    }

    @Test(priority = 210)
    public void luckyUserUserSeesOwnBetAndPointsAfterMatchFinished() {
        AuthEndPointsHandler.login(luckyUserRegData);

        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup1.getCupId());
        searchModel.setShowFinished(true);

        List<MatchBetsOnDateDTO> searchResult = MatchEndPointsHandler.searchMatches(searchModel);
        assertThat(searchResult, notNullValue());
        assertThat(searchResult.size(), is(1));

        MatchBetDTO matchBet = searchResult.get(0).getMatchBets().get(0);
        assertThat(matchBet.getMatchId(), is(cup1EarlyMatch.getMatchId()));
        assertThat(matchBet.getBet(), notNullValue());
        assertThat(matchBet.getBet().getScore1(), is(LUCKY_USER_BET_SCORE_1));
        assertThat(matchBet.getBet().getScore2(), is(LUCKY_USER_BET_SCORE_2));
        assertThat(matchBet.getBet().isSecuredBet(), is(false));

        UserMatchPointsHolderDTO holder = matchBet.getUserMatchPointsHolder();
        assertThat(holder, notNullValue());
        assertThat(holder.getMatchBetPoints(), is("2"));
        assertThat(holder.getMatchBonus(), is("0.50"));
        assertThat(holder.getSummary(), is(2.50f));
        assertThat(holder.getSummaryPoints(), is("2.50"));
        ComparisonUtils.assertEqual(holder.getUser(), luckyUser);

        ComparisonUtils.assertEqual(matchBet.getBet().getUser(), luckyUser);
    }

    private void assertEmptyPointsHolder(final MatchBetDTO matchBet, final UserDTO betOwner) {
        UserMatchPointsHolderDTO holder = matchBet.getUserMatchPointsHolder();
        assertThat(holder, notNullValue());
        assertThat(holder.getMatchBetPoints(), is("0"));
        assertThat(holder.getMatchBonus(), is("0.00"));
        assertThat(holder.getSummary(), is(0.00f));
        assertThat(holder.getSummaryPoints(), is("0.00"));
        ComparisonUtils.assertEqual(holder.getUser(), betOwner);
    }
}
