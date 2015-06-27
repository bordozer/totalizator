package user.points;

import org.junit.Test;
import totalizator.app.models.*;
import totalizator.app.services.score.CupScoresServiceImpl;

import static org.junit.Assert.assertEquals;

public class UsersScoresTest {

	@Test
	public void userGuessedRightMatchScores() {
		check( 80, 90, 6 );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose() {
		check( 81, 90, 3 );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose1() {
		check( 83, 90, 3 );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose2() {
		check( 77, 90, 3 );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose3() {
		check( 76, 90, 1 );
	}

	@Test
	public void userGuessedRightMatchWinnerAdnScoreIsVeryClose4() {
		check( 84, 90, 1 );
	}

	@Test
	public void userGuessedWrong() {
		check( 100, 90, 0 );
	}

	@Test
	public void guessedRightDrawAndScore() {
		checkDraw( 80, 80, 6 );
	}

	@Test
	public void realDrawAndBetOnDrawButAnotherScore() {
		checkDraw( 90, 90, 1 );
	}

	@Test
	public void realDrawButBetOnWinner() {
		checkDraw( 90, 80, 0 );
	}

	@Test
	public void realDrawAndBetOnDrawButAnotherScoreButVerySimilar() {
		checkDraw( 81, 81, 3 );
	}

	private void checkDraw( final int betScore1, final int betScore2, final int expectedPoints ) {
		check( 80, 80, betScore1, betScore2, expectedPoints );
	}

	private void check( final int betScore1, final int betScore2, final int expectedPoints ) {
		check( 80, 90, betScore1, betScore2, expectedPoints );
	}

	private void check( final int matchScore1, final int matchScore2, final int betScore1, final int betScore2, final int expectedPoints ) {
		final TestData testData = new TestData( matchScore1, matchScore2 );

		final CupScoresServiceImpl cupScoresService = new CupScoresServiceImpl();

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( testData.user );
		matchBet.setMatch( testData.match );
		matchBet.setBetScore1( betScore1 );
		matchBet.setBetScore2( betScore2 );

		final int points = cupScoresService.getUsersScores( matchBet );
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

			cup = new Cup( "The Cup", category );
			cup.setId( 2 );

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
