package admin.remoteGamesImport.parsing;

import org.junit.Before;
import org.junit.Test;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.matches.imports.strategies.uefa.UEFAGameParsingServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.*;

public class UEFAGameParsingServiceTest {

	private static final String REMOTE_GAMES_IDS_JSON = "{\"_links\":[{\"self\":\"http://api.football-data.org/alpha/soccerseasons/399/fixtures\"},{\"soccerseason\":\"http://api.football-data.org/alpha/soccerseasons/399\"}],\"count\":5,\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147461\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/399\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/82\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/84\"}},\"date\":\"2015-09-18T18:30:00Z\",\"status\":\"TIMED\",\"matchday\":4,\"homeTeamName\":\"Getafe CF\",\"awayTeamName\":\"Málaga CF\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147463\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/399\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/86\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/83\"}},\"date\":\"2015-09-19T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":4,\"homeTeamName\":\"Real Madrid CF\",\"awayTeamName\":\"Granada CF\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147466\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/399\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/95\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/90\"}},\"date\":\"2015-09-19T16:15:00Z\",\"status\":\"TIMED\",\"matchday\":4,\"homeTeamName\":\"Valencia CF\",\"awayTeamName\":\"Real Betis\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147460\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/399\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/278\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/78\"}},\"date\":\"2015-09-19T18:30:00Z\",\"status\":\"TIMED\",\"matchday\":4,\"homeTeamName\":\"SD Eibar\",\"awayTeamName\":\"Club Atlético de Madrid\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147464\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/399\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/92\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/80\"}},\"date\":\"2015-09-19T20:00:00Z\",\"status\":\"TIMED\",\"matchday\":4,\"homeTeamName\":\"Real Sociedad de Fútbol\",\"awayTeamName\":\"RCD Espanyol\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}}]}";
	private static final List<String> EXPECTED_REMOTE_GAME_IDS = newArrayList( "147460", "147461", "147463", "147464", "147466" );

	private static final String FINISHED_REMOTE_GAME_JSON = "{\"fixture\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147043\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2015-09-14T19:00:00Z\",\"status\":\"FINISHED\",\"matchday\":5,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"head2head\":{\"count\":10,\"timeFrameStart\":\"2009-01-10\",\"timeFrameEnd\":\"2015-09-14\",\"homeTeamWins\":3,\"awayTeamWins\":4,\"draws\":3,\"lastHomeWinHomeTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147043\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2015-09-14T19:00:00Z\",\"status\":\"FINISHED\",\"matchday\":5,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"lastWinHomeTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147043\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2015-09-14T19:00:00Z\",\"status\":\"FINISHED\",\"matchday\":5,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"lastAwayWinAwayTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/132006\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/341\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2014-01-18T15:00:00Z\",\"status\":null,\"matchday\":22,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3}},\"lastWinAwayTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136697\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-05-24T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":38,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147043\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2015-09-14T19:00:00Z\",\"status\":\"FINISHED\",\"matchday\":5,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136697\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-05-24T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":38,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136922\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2014-11-29T15:00:00Z\",\"status\":\"FINISHED\",\"matchday\":13,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/132006\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/341\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2014-01-18T15:00:00Z\",\"status\":null,\"matchday\":22,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/134685\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/341\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2013-08-23T22:00:00Z\",\"status\":null,\"matchday\":2,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/128956\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/301\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2013-05-03T22:00:00Z\",\"status\":null,\"matchday\":36,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/123660\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/301\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2012-11-10T23:00:00Z\",\"status\":null,\"matchday\":11,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64589\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/114\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2011-01-04T23:00:00Z\",\"status\":null,\"matchday\":21,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":5,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64466\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/114\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"}},\"date\":\"2010-10-23T13:30:00Z\",\"status\":null,\"matchday\":9,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":2}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/68763\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/125\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2009-01-10T14:30:00Z\",\"status\":null,\"matchday\":21,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":2}}]}}";
	private static final String FUTURE_REMOTE_GAME_JSON   = "{\"fixture\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147029\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-09-19T16:30:00Z\",\"status\":\"TIMED\",\"matchday\":6,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},\"head2head\":{\"count\":10,\"timeFrameStart\":\"2009-09-28\",\"timeFrameEnd\":\"2015-04-19\",\"homeTeamWins\":7,\"awayTeamWins\":1,\"draws\":2,\"lastHomeWinHomeTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136741\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-04-19T12:30:00Z\",\"status\":\"FINISHED\",\"matchday\":33,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"lastWinHomeTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136741\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-04-19T12:30:00Z\",\"status\":\"FINISHED\",\"matchday\":33,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},\"lastAwayWinAwayTeam\":null,\"lastWinAwayTeam\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136959\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2014-10-25T11:45:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":1}},\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136741\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2015-04-19T12:30:00Z\",\"status\":\"FINISHED\",\"matchday\":33,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/136959\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/354\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2014-10-25T11:45:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/131872\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/341\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2014-05-11T14:00:00Z\",\"status\":null,\"matchday\":38,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/132113\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/341\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2013-10-18T22:00:00Z\",\"status\":null,\"matchday\":8,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/129062\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/301\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2013-04-26T22:00:00Z\",\"status\":null,\"matchday\":35,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/123654\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/301\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2012-11-02T23:00:00Z\",\"status\":null,\"matchday\":10,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":0}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64729\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/114\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2011-04-30T22:00:00Z\",\"status\":null,\"matchday\":35,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64545\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/114\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2010-12-11T14:30:00Z\",\"status\":null,\"matchday\":17,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64379\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/113\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2010-05-08T22:00:00Z\",\"status\":null,\"matchday\":38,\"homeTeamName\":\"West Ham United FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/64066\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/113\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"}},\"date\":\"2009-09-27T22:00:00Z\",\"status\":null,\"matchday\":7,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":3,\"goalsAwayTeam\":1}}]}}";

	private TestData testData;

	@Before
	public void setup() {
		testData = new TestData();
	}

	@Test
	public void extractRemoteGameIds() {

		final RemoteGameParsingService uefaGameParsingService = new UEFAGameParsingServiceImpl();
		final List<RemoteGame> remoteGames = newArrayList( uefaGameParsingService.loadGamesFromJSON( testData.cup, REMOTE_GAMES_IDS_JSON ) );

		assertEquals( remoteGames.size(), EXPECTED_REMOTE_GAME_IDS.size() );

		for ( final String remoteGameId : EXPECTED_REMOTE_GAME_IDS ) {
			assertTrue( doesGameIdPresent( remoteGames, remoteGameId ) );
		}

		int i = 0;
		for ( final RemoteGame remoteGame : remoteGames ) {
			assertEquals( EXPECTED_REMOTE_GAME_IDS.get( i ), remoteGame.getRemoteGameId() );

			i++;
		}

		final RemoteGame remoteGame = remoteGames.get( 2 );
		assertEquals( "147463", remoteGame.getRemoteGameId() );

		assertEquals( "Real Madrid CF", remoteGame.getRemoteTeam1Id() );
		assertEquals( "Real Madrid CF", remoteGame.getRemoteTeam1Name() );

		assertEquals( "Granada CF", remoteGame.getRemoteTeam2Id() );
		assertEquals( "Granada CF", remoteGame.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-09-19T14:00" ), remoteGame.getBeginningTime() );
		assertEquals( 1, remoteGame.getHomeTeamNumber() );

		assertEquals( 0, remoteGame.getScore1() );
		assertEquals( 0, remoteGame.getScore2() );

		assertFalse( remoteGame.isFinished() );

		assertTrue( remoteGame.isLoaded() );
	}

	@Test
	public void parseFinishedGame() throws IOException {

		final String remoteGameId = "JSON DOES NOT CONTAIN IT";

		final RemoteGameParsingService uefaGameParsingService = new UEFAGameParsingServiceImpl();

		final RemoteGame remoteGame = new RemoteGame( remoteGameId );
		uefaGameParsingService.loadGameFromJSON( remoteGame, FINISHED_REMOTE_GAME_JSON );

		assertEquals( remoteGame.getRemoteGameId(), remoteGameId );

		assertEquals( remoteGame.getRemoteTeam1Id(), "West Ham United FC" );
		assertEquals( remoteGame.getRemoteTeam1Name(), "West Ham United FC" );

		assertEquals( remoteGame.getRemoteTeam2Id(), "Newcastle United FC" );
		assertEquals( remoteGame.getRemoteTeam2Name(), "Newcastle United FC" );

		assertEquals( remoteGame.getBeginningTime(), LocalDateTime.parse( "2015-09-14T19:00" ) );
		assertEquals( remoteGame.getHomeTeamNumber(), 1 );

		assertEquals( remoteGame.getScore1(), 2 );
		assertEquals( remoteGame.getScore2(), 0 );

		assertTrue( remoteGame.isFinished() );

		assertTrue( remoteGame.isLoaded() );
	}

	@Test
	public void parseFutureGame() throws IOException {

		final String remoteGameId = "JSON DOES NOT CONTAIN IT";

		final RemoteGameParsingService uefaGameParsingService = new UEFAGameParsingServiceImpl();

		final RemoteGame remoteGame = new RemoteGame( remoteGameId );
		uefaGameParsingService.loadGameFromJSON( remoteGame, FUTURE_REMOTE_GAME_JSON );

		assertEquals( remoteGame.getRemoteGameId(), remoteGameId );

		assertEquals( remoteGame.getRemoteTeam1Id(), "Manchester City FC" );
		assertEquals( remoteGame.getRemoteTeam1Name(), "Manchester City FC" );

		assertEquals( remoteGame.getRemoteTeam2Id(), "West Ham United FC" );
		assertEquals( remoteGame.getRemoteTeam2Name(), "West Ham United FC" );

		assertEquals( remoteGame.getBeginningTime(), LocalDateTime.parse( "2015-09-19T16:30" ) );
		assertEquals( remoteGame.getHomeTeamNumber(), 1 );

		assertEquals( remoteGame.getScore1(), 0 );
		assertEquals( remoteGame.getScore2(), 0 );

		assertFalse( remoteGame.isFinished() );

		assertTrue( remoteGame.isLoaded() );
	}

	private boolean doesGameIdPresent( final List<RemoteGame> remoteGames, final String remoteGameId ) {

		for ( final RemoteGame gameId : remoteGames ) {
			if ( gameId.getRemoteGameId().equals( remoteGameId ) ) {
				return true;
			}
		}

		return false;
	}

	private class TestData {

		private Cup cup;

		public TestData() {

			cup = new Cup();
			cup.setId( 555 );
			cup.setCupImportId( "77777777" );
		}
	}
}
