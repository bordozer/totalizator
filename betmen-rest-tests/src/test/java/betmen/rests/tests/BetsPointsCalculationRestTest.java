package betmen.rests.tests;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UsersRatingPositionDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import betmen.dto.dto.points.scores.CupUsersScoresDTO;
import betmen.dto.dto.points.scores.CupUsersScoresInTimeDTO;
import betmen.dto.dto.points.scores.UserMatchesPointsDTO;
import betmen.dto.dto.points.scores.UserRatingPositionDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.UsersPointsEndpointHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.eq;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BetsPointsCalculationRestTest {

    private static final LocalDateTime TIME_IN_1_DAY_1 = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime TIME_IN_1_DAY_2 = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime TIME_IN_5_DAYS = LocalDateTime.now().plusDays(5);
    private static final LocalDateTime TIME_IN_2_MONTH = LocalDateTime.now().plusMonths(2);

    private UserRegData userData1;
    private UserRegData userData2;
    private UserRegData userData3;
    private UserRegData userData4;

    private UserDTO user1;
    private UserDTO user2;
    private UserDTO user3;
    private UserDTO user4;

    private SportKindEditDTO sport;
    private CategoryEditDTO category;

    private TeamEditDTO team1;
    private TeamEditDTO team2;
    private TeamEditDTO team3;
    private TeamEditDTO team4;
    private TeamEditDTO team5;
    private TeamEditDTO team6;

    private CupEditDTO cup;

    private MatchEditDTO match1;
    private MatchEditDTO match2;
    private MatchEditDTO match3;
    private MatchEditDTO match4;

    private BetDTO user1match1Bet;
    private BetDTO user1match2Bet;
    private BetDTO user1match3Bet;
    private BetDTO user1match4Bet;

    private BetDTO user2match1Bet;
    private BetDTO user2match2Bet;
    private BetDTO user2match3Bet;
    private BetDTO user2match4Bet;

    private BetDTO user3match1Bet;
    private BetDTO user3match2Bet;
    private BetDTO user3match3Bet;
    private BetDTO user3match4Bet;

    private BetDTO user4match1Bet;
    private BetDTO user4match2Bet;
    private BetDTO user4match3Bet;
    private BetDTO user4match4Bet;

    @BeforeClass
    public void initClass() {
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();
        sport = AdminTestDataGenerator.createSport();
        category = AdminTestDataGenerator.createCategory(sport.getSportKindId());

        team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team3 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team4 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team5 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team6 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        cup = createCup((cupTemplater(category, sp).publicCup().inHour(12).build()));

        match1 = createMatch(team1.getTeamId(), team2.getTeamId(), TIME_IN_1_DAY_1);
        match2 = createMatch(team3.getTeamId(), team4.getTeamId(), TIME_IN_1_DAY_2);
        match3 = createMatch(team5.getTeamId(), team6.getTeamId(), TIME_IN_5_DAYS);
        match4 = createMatch(team1.getTeamId(), team6.getTeamId(), TIME_IN_2_MONTH);

        AuthEndPointsHandler.logout();

        userData1 = RandomUtils.randomUser();
        user1 = AuthEndPointsHandler.registerNewUser(userData1);
        userData2 = RandomUtils.randomUser();
        user2 = AuthEndPointsHandler.registerNewUser(userData2);
        userData3 = RandomUtils.randomUser();
        user3 = AuthEndPointsHandler.registerNewUser(userData3);
        userData4 = RandomUtils.randomUser();
        user4 = AuthEndPointsHandler.registerNewUser(userData4);

        AuthEndPointsHandler.login(userData1);
        user1match1Bet = BetEndPointsHandler.make(match1.getMatchId(), 100, 110);
        user1match2Bet = BetEndPointsHandler.make(match2.getMatchId(), 90, 90);
        user1match3Bet = BetEndPointsHandler.make(match3.getMatchId(), 80, 80);
        user1match4Bet = BetEndPointsHandler.make(match4.getMatchId(), 120, 70);
        AuthEndPointsHandler.logout();

        AuthEndPointsHandler.login(userData2);
        user2match1Bet = BetEndPointsHandler.make(match1.getMatchId(), 103, 107);
        user2match2Bet = BetEndPointsHandler.make(match2.getMatchId(), 100, 100);
        user2match3Bet = BetEndPointsHandler.make(match3.getMatchId(), 80, 80);
        user2match4Bet = BetEndPointsHandler.make(match4.getMatchId(), 120, 70);
        AuthEndPointsHandler.logout();

        AuthEndPointsHandler.login(userData3);
        user3match1Bet = BetEndPointsHandler.make(match1.getMatchId(), 90, 100);
        user3match2Bet = BetEndPointsHandler.make(match2.getMatchId(), 94, 89);
        user3match3Bet = BetEndPointsHandler.make(match3.getMatchId(), 80, 80);
        user3match4Bet = BetEndPointsHandler.make(match4.getMatchId(), 120, 70);
        AuthEndPointsHandler.logout();

        AuthEndPointsHandler.login(userData4);
        user4match1Bet = BetEndPointsHandler.make(match1.getMatchId(), 100, 90);
        user4match2Bet = BetEndPointsHandler.make(match2.getMatchId(), 91, 92);
        user4match3Bet = BetEndPointsHandler.make(match3.getMatchId(), 80, 80);
        user4match4Bet = BetEndPointsHandler.make(match4.getMatchId(), 120, 70);
        AuthEndPointsHandler.logout();

        AuthEndPointsHandler.loginAsAdmin();
        finishMatch(this.match1, 100, 110);
        finishMatch(this.match2, 95, 90);
        finishMatch(this.match3, 80, 80);
        finishMatch(this.match4, 70, 120);
        AuthEndPointsHandler.logout();
    }

    @BeforeMethod
    public void beforeTest() {
        AuthEndPointsHandler.login(userData1);
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldCalculatePointsForMatch1() {
        assertMatchPoints(this.match1, user1.getUserId(), "6");
        assertMatchBonuses(this.match1, user1.getUserId(), "0.33");
        assertSummaryMatchPoints(this.match1, user1.getUserId(), "6.33");

        assertMatchPoints(this.match1, user2.getUserId(), "2");
        assertMatchBonuses(this.match1, user2.getUserId(), "0.33");
        assertSummaryMatchPoints(this.match1, user2.getUserId(), "2.33");

        assertMatchPoints(this.match1, user3.getUserId(), "1");
        assertMatchBonuses(this.match1, user3.getUserId(), "0.33");
        assertSummaryMatchPoints(this.match1, user3.getUserId(), "1.33");

        assertMatchPoints(this.match1, user4.getUserId(), "-1");
        assertMatchBonuses(this.match1, user4.getUserId(), "0.00");
        assertSummaryMatchPoints(this.match1, user4.getUserId(), "-1.00");
    }

    @Test
    public void shouldCalculatePointsForMatch2() {
        assertMatchPoints(this.match2, user1.getUserId(), "-1");
        assertMatchBonuses(this.match2, user1.getUserId(), "0.00");
        assertSummaryMatchPoints(match2, user1.getUserId(), "-1.00");

        assertMatchPoints(this.match2, user2.getUserId(), "-1");
        assertMatchBonuses(this.match2, user2.getUserId(), "0.00");
        assertSummaryMatchPoints(match2, user2.getUserId(), "-1.00");

        assertMatchPoints(this.match2, user3.getUserId(), "2");
        assertMatchBonuses(this.match2, user3.getUserId(), "3.00");
        assertSummaryMatchPoints(match2, user3.getUserId(), "5.00");

        assertMatchPoints(this.match2, user4.getUserId(), "-1");
        assertMatchBonuses(this.match2, user4.getUserId(), "0.00");
        assertSummaryMatchPoints(match2, user4.getUserId(), "-1.00");
    }

    @Test
    public void shouldCalculatePointsForMatch3() {
        assertMatchPoints(this.match3, user1.getUserId(), "6");
        assertMatchBonuses(this.match3, user1.getUserId(), "0.00");
        assertSummaryMatchPoints(match3, user1.getUserId(), "6.00");

        assertMatchPoints(this.match3, user2.getUserId(), "6");
        assertMatchBonuses(this.match3, user2.getUserId(), "0.00");
        assertSummaryMatchPoints(match3, user2.getUserId(), "6.00");

        assertMatchPoints(this.match3, user3.getUserId(), "6");
        assertMatchBonuses(this.match3, user3.getUserId(), "0.00");
        assertSummaryMatchPoints(match3, user3.getUserId(), "6.00");

        assertMatchPoints(this.match3, user4.getUserId(), "6");
        assertMatchBonuses(this.match3, user4.getUserId(), "0.00");
        assertSummaryMatchPoints(match3, user4.getUserId(), "6.00");
    }

    @Test
    public void shouldCalculatePointsForMatch4() {
        assertMatchPoints(this.match4, user1.getUserId(), "-1");
        assertMatchBonuses(this.match4, user1.getUserId(), "0.00");
        assertSummaryMatchPoints(match4, user1.getUserId(), "-1.00");

        assertMatchPoints(this.match4, user2.getUserId(), "-1");
        assertMatchBonuses(this.match4, user2.getUserId(), "0.00");
        assertSummaryMatchPoints(match4, user2.getUserId(), "-1.00");

        assertMatchPoints(this.match4, user3.getUserId(), "-1");
        assertMatchBonuses(this.match4, user3.getUserId(), "0.00");
        assertSummaryMatchPoints(match4, user3.getUserId(), "-1.00");

        assertMatchPoints(this.match4, user4.getUserId(), "-1");
        assertMatchBonuses(this.match4, user4.getUserId(), "0.00");
        assertSummaryMatchPoints(match4, user4.getUserId(), "-1.00");
    }

    @Test
    public void shouldCalculateCupPoints() {
        CupUsersScoresDTO cupScores = UsersPointsEndpointHandler.getCupScores(cup.getCupId());
        assertThat(cupScores, notNullValue());
        assertThat(eq(cupScores.getCurrentUser()), is(eq(user1)));

        List<UserRatingPositionDTO> ratingPositions = cupScores.getUserRatingPositions();
        assertThat(ratingPositions, notNullValue());
        assertThat(ratingPositions, hasSize(4));

        UserRatingPositionDTO ratingPosition1 = ratingPositions.get(0);
        assertThat(ratingPosition1, notNullValue());
        assertThat(eq(ratingPosition1.getUser()), is(eq(user3)));
        assertThat(ratingPosition1.getUserCupPointsHolder().getMatchBetPointsPositive(), is("9"));
        assertThat(ratingPosition1.getUserCupPointsHolder().getMatchBetPointsNegative(), is("-1"));
        assertThat(ratingPosition1.getUserCupPointsHolder().getMatchBetPoints(), is("8"));
        assertThat(ratingPosition1.getUserCupPointsHolder().getMatchBonuses(), is("3.33"));
        assertThat(ratingPosition1.getUserCupPointsHolder().getCupWinnerBonus(), is("0"));
        assertThat(ratingPosition1.getUserCupPointsHolder().getSummaryPoints(), is("11.33"));

        UserRatingPositionDTO ratingPosition2 = ratingPositions.get(1);
        assertThat(ratingPosition2, notNullValue());
        assertThat(eq(ratingPosition2.getUser()), is(eq(user1)));
        assertThat(ratingPosition2.getUserCupPointsHolder().getMatchBetPointsPositive(), is("12"));
        assertThat(ratingPosition2.getUserCupPointsHolder().getMatchBetPointsNegative(), is("-2"));
        assertThat(ratingPosition2.getUserCupPointsHolder().getMatchBetPoints(), is("10"));
        assertThat(ratingPosition2.getUserCupPointsHolder().getMatchBonuses(), is("0.33"));
        assertThat(ratingPosition2.getUserCupPointsHolder().getCupWinnerBonus(), is("0"));
        assertThat(ratingPosition2.getUserCupPointsHolder().getSummaryPoints(), is("10.33"));

        UserRatingPositionDTO ratingPosition3 = ratingPositions.get(2);
        assertThat(ratingPosition3, notNullValue());
        assertThat(eq(ratingPosition3.getUser()), is(eq(user2)));
        assertThat(ratingPosition3.getUserCupPointsHolder().getMatchBetPointsPositive(), is("8"));
        assertThat(ratingPosition3.getUserCupPointsHolder().getMatchBetPointsNegative(), is("-2"));
        assertThat(ratingPosition3.getUserCupPointsHolder().getMatchBetPoints(), is("6"));
        assertThat(ratingPosition3.getUserCupPointsHolder().getMatchBonuses(), is("0.33"));
        assertThat(ratingPosition3.getUserCupPointsHolder().getCupWinnerBonus(), is("0"));
        assertThat(ratingPosition3.getUserCupPointsHolder().getSummaryPoints(), is("6.33"));

        UserRatingPositionDTO ratingPosition4 = ratingPositions.get(3);
        assertThat(ratingPosition4, notNullValue());
        assertThat(eq(ratingPosition4.getUser()), is(eq(user4)));
        assertThat(ratingPosition4.getUserCupPointsHolder().getMatchBetPointsPositive(), is("6"));
        assertThat(ratingPosition4.getUserCupPointsHolder().getMatchBetPointsNegative(), is("-3"));
        assertThat(ratingPosition4.getUserCupPointsHolder().getMatchBetPoints(), is("3"));
        assertThat(ratingPosition4.getUserCupPointsHolder().getMatchBonuses(), is("0.00"));
        assertThat(ratingPosition4.getUserCupPointsHolder().getCupWinnerBonus(), is("0"));
        assertThat(ratingPosition4.getUserCupPointsHolder().getSummaryPoints(), is("3.00"));
    }

    @Test
    public void shouldCalculateCupPointsInTime() {
        CupUsersScoresInTimeDTO cupScores = UsersPointsEndpointHandler.getCupScoresInTime(cup.getCupId());
        assertThat(cupScores, notNullValue());

        List<String> dates = cupScores.getDates();
        assertThat(dates, notNullValue());
        assertThat(dates, hasSize(3));
        assertThat(eq(dates.get(0)), is(eq(toDate(TIME_IN_1_DAY_1))));
        assertThat(eq(dates.get(1)), is(eq(toDate(TIME_IN_5_DAYS))));
        assertThat(eq(dates.get(2)), is(eq(toDate(TIME_IN_2_MONTH))));

        Map<Integer, UserMatchesPointsDTO> map = cupScores.getUsersPointsMap();
        assertThat(map, notNullValue());

        UserMatchesPointsDTO pointsDTO1 = map.get(user1.getUserId());
        assertThat(pointsDTO1, notNullValue());
        assertThat(pointsDTO1.getUser(), is(user1));
        List<UserMatchPointsHolderDTO> matchesPoints1 = pointsDTO1.getUserMatchesPoints();
        assertThat(matchesPoints1, notNullValue());
        assertThat(matchesPoints1, hasSize(3));
        assertThat(matchesPoints1.get(0).getUser(), is(user1));
        assertThat(matchesPoints1.get(0).getMatchBetPoints(), is("5"));
        assertThat(matchesPoints1.get(0).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints1.get(0).getSummaryPoints(), is("5.33"));
        assertThat(matchesPoints1.get(1).getMatchBetPoints(), is("11"));
        assertThat(matchesPoints1.get(1).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints1.get(1).getSummaryPoints(), is("11.33"));
        assertThat(matchesPoints1.get(2).getMatchBetPoints(), is("10"));
        assertThat(matchesPoints1.get(2).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints1.get(2).getSummaryPoints(), is("10.33"));

        UserMatchesPointsDTO pointsDTO2 = map.get(user2.getUserId());
        assertThat(pointsDTO2, notNullValue());
        assertThat(pointsDTO1.getUser(), is(user1));
        List<UserMatchPointsHolderDTO> matchesPoints2 = pointsDTO2.getUserMatchesPoints();
        assertThat(matchesPoints2, notNullValue());
        assertThat(matchesPoints2, hasSize(3));
        assertThat(matchesPoints2.get(0).getUser(), is(user2));
        assertThat(matchesPoints2.get(0).getMatchBetPoints(), is("1"));
        assertThat(matchesPoints2.get(0).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints2.get(0).getSummaryPoints(), is("1.33"));
        assertThat(matchesPoints2.get(1).getMatchBetPoints(), is("7"));
        assertThat(matchesPoints2.get(1).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints2.get(1).getSummaryPoints(), is("7.33"));
        assertThat(matchesPoints2.get(2).getMatchBetPoints(), is("6"));
        assertThat(matchesPoints2.get(2).getMatchBonus(), is("0.33"));
        assertThat(matchesPoints2.get(2).getSummaryPoints(), is("6.33"));


        UserMatchesPointsDTO pointsDTO3 = map.get(user3.getUserId());
        assertThat(pointsDTO3, notNullValue());
        assertThat(pointsDTO1.getUser(), is(user1));
        List<UserMatchPointsHolderDTO> matchesPoints3 = pointsDTO3.getUserMatchesPoints();
        assertThat(matchesPoints3, notNullValue());
        assertThat(matchesPoints3, hasSize(3));
        assertThat(matchesPoints3.get(0).getUser(), is(user3));
        assertThat(matchesPoints3.get(0).getMatchBetPoints(), is("3"));
        assertThat(matchesPoints3.get(0).getMatchBonus(), is("3.33"));
        assertThat(matchesPoints3.get(0).getSummaryPoints(), is("6.33"));
        assertThat(matchesPoints3.get(1).getMatchBetPoints(), is("9"));
        assertThat(matchesPoints3.get(1).getMatchBonus(), is("3.33"));
        assertThat(matchesPoints3.get(1).getSummaryPoints(), is("12.33"));
        assertThat(matchesPoints3.get(2).getMatchBetPoints(), is("8"));
        assertThat(matchesPoints3.get(2).getMatchBonus(), is("3.33"));
        assertThat(matchesPoints3.get(2).getSummaryPoints(), is("11.33"));

        UserMatchesPointsDTO pointsDTO4 = map.get(user4.getUserId());
        assertThat(pointsDTO4, notNullValue());
        assertThat(pointsDTO1.getUser(), is(user1));
        List<UserMatchPointsHolderDTO> matchesPoints4 = pointsDTO4.getUserMatchesPoints();
        assertThat(matchesPoints4, notNullValue());
        assertThat(matchesPoints4, hasSize(3));
        assertThat(matchesPoints4.get(0).getUser(), is(user4));
        assertThat(matchesPoints4.get(0).getMatchBetPoints(), is("-2"));
        assertThat(matchesPoints4.get(0).getMatchBonus(), is("0.00"));
        assertThat(matchesPoints4.get(0).getSummaryPoints(), is("-2.00"));
        assertThat(matchesPoints4.get(1).getMatchBetPoints(), is("4"));
        assertThat(matchesPoints4.get(1).getMatchBonus(), is("0.00"));
        assertThat(matchesPoints4.get(1).getSummaryPoints(), is("4.00"));
        assertThat(matchesPoints4.get(2).getMatchBetPoints(), is("3"));
        assertThat(matchesPoints4.get(2).getMatchBonus(), is("0.00"));
        assertThat(matchesPoints4.get(2).getSummaryPoints(), is("3.00"));
    }

    @Test
    public void shouldCalculateUserPointsForDateRange1() {
        List<UsersRatingPositionDTO> usersRating = UsersPointsEndpointHandler.getUsersRating(TIME_IN_1_DAY_1.toLocalDate(), TIME_IN_1_DAY_2.toLocalDate());
        assertThat(usersRating, notNullValue());
        assertThat(usersRating, hasSize(4));

        UsersRatingPositionDTO position1 = usersRating.get(0);
        assertThat(position1.getUser(), is(user3));
        assertThat(position1.getBetPoints(), is("3"));
        assertThat(position1.getMatchBonus(), is("3.33"));
        assertThat(position1.getPointsSummary(), is("6.33"));

        UsersRatingPositionDTO position2 = usersRating.get(1);
        assertThat(position2.getUser(), is(user1));
        assertThat(position2.getBetPoints(), is("5"));
        assertThat(position2.getMatchBonus(), is("0.33"));
        assertThat(position2.getPointsSummary(), is("5.33"));

        UsersRatingPositionDTO position3 = usersRating.get(2);
        assertThat(position3.getUser(), is(user2));
        assertThat(position3.getBetPoints(), is("1"));
        assertThat(position3.getMatchBonus(), is("0.33"));
        assertThat(position3.getPointsSummary(), is("1.33"));

        UsersRatingPositionDTO position4 = usersRating.get(3);
        assertThat(position4.getUser(), is(user4));
        assertThat(position4.getBetPoints(), is("-2"));
        assertThat(position4.getMatchBonus(), is("0.00"));
        assertThat(position4.getPointsSummary(), is("-2.00"));
    }

    @Test
    public void shouldCalculateUserPointsForDateRange2() {
        List<UsersRatingPositionDTO> usersRating = UsersPointsEndpointHandler.getUsersRating(TIME_IN_1_DAY_1.toLocalDate(), TIME_IN_5_DAYS.toLocalDate());
        assertThat(usersRating, notNullValue());
        assertThat(usersRating, hasSize(4));

        UsersRatingPositionDTO position1 = usersRating.get(0);
        assertThat(position1.getUser(), is(user3));
        assertThat(position1.getBetPoints(), is("9"));
        assertThat(position1.getMatchBonus(), is("3.33"));
        assertThat(position1.getPointsSummary(), is("12.33"));

        UsersRatingPositionDTO position2 = usersRating.get(1);
        assertThat(position2.getUser(), is(user1));
        assertThat(position2.getBetPoints(), is("11"));
        assertThat(position2.getMatchBonus(), is("0.33"));
        assertThat(position2.getPointsSummary(), is("11.33"));

        UsersRatingPositionDTO position3 = usersRating.get(2);
        assertThat(position3.getUser(), is(user2));
        assertThat(position3.getBetPoints(), is("7"));
        assertThat(position3.getMatchBonus(), is("0.33"));
        assertThat(position3.getPointsSummary(), is("7.33"));

        UsersRatingPositionDTO position4 = usersRating.get(3);
        assertThat(position4.getUser(), is(user4));
        assertThat(position4.getBetPoints(), is("4"));
        assertThat(position4.getMatchBonus(), is("0.00"));
        assertThat(position4.getPointsSummary(), is("4.00"));
    }

    // TODO: check points for groups
    // TODO: depends from user group

    // TODO: finish cup and check cup points
    // TODO: depends from user cup winner

    private LocalDate toDate(final LocalDateTime time) {
        return LocalDate.of(time.getYear(), time.getMonth(), time.getDayOfMonth());
    }

    private void assertMatchPoints(final MatchEditDTO match, final int userId, final String points) {
        assertThat(BetEndPointsHandler.get(match.getMatchId(), userId).getUserMatchPointsHolder().getMatchBetPoints(), is(points));
    }

    private void assertMatchBonuses(final MatchEditDTO match, final int userId, final String points) {
        assertThat(BetEndPointsHandler.get(match.getMatchId(), userId).getUserMatchPointsHolder().getMatchBonus(), is(points));
    }

    private void assertSummaryMatchPoints(final MatchEditDTO match, final int userId, final String points) {
        assertThat(BetEndPointsHandler.get(match.getMatchId(), userId).getUserMatchPointsHolder().getSummaryPoints(), is(points));
    }

    private void finishMatch(final MatchEditDTO match, final int score1, final int score2) {
        match.setScore1(score1);
        match.setScore2(score2);
        match.setMatchFinished(true);
        AdminMatchEndPointsHandler.update(match);
    }

    private MatchEditDTO createMatch(final int teamId, final int teamId2, final LocalDateTime beginningTime) {
        MatchEditDTO dto = MatchTemplater.random(cup.getCupId(), teamId, teamId2).builder().finished(false).withBeginningTime(beginningTime).build();
        return AdminMatchEndPointsHandler.create(dto);
    }

    private CupTemplater cupTemplater(final CategoryEditDTO category, final PointsCalculationStrategyEditDTO ps) {
        return CupTemplater.random(category.getCategoryId(), ps.getPcsId());
    }

    private CupEditDTO createCup(final CupEditDTO cup) {
        return AdminCupEndPointsHandler.create(cup);
    }
}
