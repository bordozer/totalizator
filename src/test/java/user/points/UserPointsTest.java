package user.points;

import org.easymock.EasyMock;
import org.junit.Test;
import totalizator.app.beans.UserPoints;
import totalizator.app.models.*;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.score.CupScoresServiceImpl;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class UserPointsTest {

	@Test
	public void check() {

		final TestData testData = new TestData();

		final CupScoresServiceImpl cupScoresService = new CupScoresServiceImpl();
		cupScoresService.setMatchBetsService( matchBetsService( testData ) );

		final List<UserPoints> usersScores = cupScoresService.getUserPoints( testData.cup, testData.user );

		assertEquals( "Wrong points for match bet", 6, usersScores.get( 0 ).getPoints() );
		assertEquals( "Wrong points for match bet", 1, usersScores.get( 1 ).getPoints() );
	}

	private MatchBetsService matchBetsService( final TestData testData ) {

		final MatchBetsService matchBetsService = EasyMock.createMock( MatchBetsService.class );

		EasyMock.expect( matchBetsService.loadAll( testData.cup, testData.user ) ).andReturn( testData.matchBets ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( matchBetsService );

		return matchBetsService;
	}

	private class TestData {

		private final User user;

		private final Cup cup;
		private final Category category;

		private final List<MatchBet> matchBets = newArrayList();

		private TestData() {

			user = new User();
			user.setId( 1000 );
			user.setUsername( "The User" );

			category = new Category( "NBA" );
			category.setId( 1 );

			final PointsCalculationStrategy pointsCalculationStrategy = new PointsCalculationStrategy();
			pointsCalculationStrategy.setStrategyName( "Abstract strategy" );
			pointsCalculationStrategy.setPointsForMatchScore( 6 );
			pointsCalculationStrategy.setPointsForMatchWinner( 1 );
			pointsCalculationStrategy.setPointsDelta( 3 );
			pointsCalculationStrategy.setPointsForBetWithinDelta( 3 );

			cup = new Cup( "The Cup", category );
			cup.setId( 2 );
			cup.setPointsCalculationStrategy( pointsCalculationStrategy );

			final Team team1 = new Team( "Team 1", category );
			team1.setId( 101 );

			final Team team2 = new Team( "Team 2", category );
			team2.setId( 102 );

			final Team team3 = new Team( "Team 3", category );
			team2.setId( 103 );

			final Team team4 = new Team( "Team 4", category );
			team2.setId( 104 );

			final MatchBet matchBet1 = new MatchBet();
			matchBet1.setId( 10000 );
			matchBet1.setUser( user );
			matchBet1.setBetScore1( 100 );
			matchBet1.setBetScore2( 110 );
			matchBet1.setMatch( getMatch( team1, team2, 100, 110 ) );

			matchBets.add( matchBet1 );

			final MatchBet matchBet2 = new MatchBet();
			matchBet2.setId( 10000 );
			matchBet2.setUser( user );
			matchBet2.setBetScore1( 100 );
			matchBet2.setBetScore2( 110 );
			matchBet2.setMatch( getMatch( team3, team4, 100, 120 ) );

			matchBets.add( matchBet2 );
		}

		private Match getMatch( final Team team1, final Team team2, final int score1, final int score2 ) {

			final Match match = new Match();

			match.setId( 10001 );
			match.setCup( cup );
			match.setTeam1( team1 );
			match.setTeam2( team2 );
			match.setMatchFinished( true );
			match.setScore1( score1 );
			match.setScore2( score2 );

			return match;
		}
	}
}
