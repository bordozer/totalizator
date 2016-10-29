package betmen.core.user.points;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.points.calculation.match.points.UserMatchBetPointsCalculationServiceImpl;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class UsersScoresTest {

	public static final int POINTS_FOR_MATCH_SCORE = 6;
	public static final int POINTS_FOR_MATCH_WINNER = 1;
	public static final int POINTS_DELTA = 3;
	public static final int POINTS_FOR_BET_WITHIN_DELTA = 3;

	@Test
	public void userGuessedRightMatchScores() {
		check( 80, 90, POINTS_FOR_MATCH_SCORE );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose() {
		check( 81, 90, POINTS_FOR_BET_WITHIN_DELTA );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose1() {
		check( 83, 90, POINTS_FOR_BET_WITHIN_DELTA );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose2() {
		check( 77, 90, POINTS_FOR_BET_WITHIN_DELTA );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose3() {
		check( 76, 90, POINTS_FOR_MATCH_WINNER );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose4() {
		check( 84, 90, POINTS_FOR_MATCH_WINNER );
	}

	@Test
	public void userGuessedWrong() {
		check( 100, 90, -POINTS_FOR_MATCH_WINNER );
	}

	@Test
	public void guessedRightDrawAndScore() {
		checkDraw( 80, 80, POINTS_FOR_MATCH_SCORE );
	}

	@Test
	public void realDrawAndBetOnDrawButAnotherScore() {
		checkDraw( 90, 90, POINTS_FOR_MATCH_WINNER );
	}

	@Test
	public void realDrawButBetOnWinner() {
		checkDraw( 90, 80, -POINTS_FOR_MATCH_WINNER );
	}

	@Test
	public void realDrawAndBetOnDrawButAnotherScoreButVerySimilar() {
		checkDraw( 81, 81, POINTS_FOR_BET_WITHIN_DELTA );
	}

	private void checkDraw( final int betScore1, final int betScore2, final float expectedPoints ) {
		check( 80, 80, betScore1, betScore2, expectedPoints );
	}

	private void check( final int betScore1, final int betScore2, final float expectedPoints ) {
		check( 80, 90, betScore1, betScore2, expectedPoints );
	}

	private void check( final int matchScore1, final int matchScore2, final int betScore1, final int betScore2, final float expectedPoints ) {
		final TestData testData = new TestData( matchScore1, matchScore2 );

		final UserMatchBetPointsCalculationServiceImpl userMatchBetPointsCalculationService = new UserMatchBetPointsCalculationServiceImpl();

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( testData.user );
		matchBet.setMatch( testData.match );
		matchBet.setBetScore1( betScore1 );
		matchBet.setBetScore2( betScore2 );

		final float points = userMatchBetPointsCalculationService.getUserMatchBetPoints( matchBet ).getPoints();
		assertEquals( "Wrong points for match bet", expectedPoints, points );
	}

	private class TestData {

		private final User user;

		private final Cup cup;
		private final Category category;

		private final Match match;
		private final Team team1;
		private final Team team2;

		private TestData( int score1, int score2 ) {

			user = new User();
			user.setId( 1000 );
			user.setUsername( "The User" );

			category = new Category( "NBA" );
			category.setId( 1 );

			final PointsCalculationStrategy pointsCalculationStrategy = new PointsCalculationStrategy();
			pointsCalculationStrategy.setStrategyName( "Abstract strategy" );
			pointsCalculationStrategy.setPointsForMatchScore( POINTS_FOR_MATCH_SCORE );
			pointsCalculationStrategy.setPointsForMatchWinner( POINTS_FOR_MATCH_WINNER );
			pointsCalculationStrategy.setPointsDelta( POINTS_DELTA );
			pointsCalculationStrategy.setPointsForBetWithinDelta( POINTS_FOR_BET_WITHIN_DELTA );

			cup = new Cup( "The Cup", category );
			cup.setId( 2 );
			cup.setPointsCalculationStrategy( pointsCalculationStrategy );

			team1 = new Team( "Winner", category );
			team1.setId( 100 );

			team2 = new Team( "Looser", category );
			team2.setId( 101 );

			match = new Match();
			match.setId( 10001 );
			match.setCup( cup );
			match.setTeam1( team1 );
			match.setTeam2( team2 );
			match.setMatchFinished( true );
			match.setScore1( score1 );
			match.setScore2( score2 );
		}
	}
}
