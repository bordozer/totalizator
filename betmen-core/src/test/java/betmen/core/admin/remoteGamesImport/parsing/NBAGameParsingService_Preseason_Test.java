package betmen.core.admin.remoteGamesImport.parsing;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.strategies.nba.NBAGameParsingService;
import betmen.core.service.utils.DateTimeServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.*;

public class NBAGameParsingService_Preseason_Test {

	private static final List<String> PRESEASON_EXPECTED_REMOTE_GAME_IDS = newArrayList( "0011500004", "0011500005", "0011500006" );

	private TestData testData;

	@Before
	public void setup() {
		testData = new TestData();
	}

	@Test
	public void extractRemoteGameIds() {

		final NBAGameParsingService nbaGameParsingService = new NBAGameParsingService();
		nbaGameParsingService.setDateTimeService( new DateTimeServiceImpl() );

		final Set<RemoteGame> remoteGameIds = nbaGameParsingService.loadGamesFromJSON( testData.cup, PRESEASON_REMOTE_GAMES_IDS_JSON );

		assertEquals( remoteGameIds.size(), PRESEASON_EXPECTED_REMOTE_GAME_IDS.size() );

		for ( final String remoteGameId : PRESEASON_EXPECTED_REMOTE_GAME_IDS ) {
			assertTrue( doesGameIdPresent( remoteGameIds, remoteGameId ) );
		}

		int i = 0;
		for ( final RemoteGame remoteGame : remoteGameIds ) {
			assertEquals( PRESEASON_EXPECTED_REMOTE_GAME_IDS.get( i ), remoteGame.getRemoteGameId() );

			i++;
		}
	}

	@Test
	public void parsePreseasonFinishedGame() throws IOException {

		final String remoteGameId = "0011400001";

		final NBAGameParsingService nbaGameParsingService = new NBAGameParsingService();
		nbaGameParsingService.setDateTimeService( new DateTimeServiceImpl() );

		final RemoteGame remoteGame = new RemoteGame( remoteGameId );
		nbaGameParsingService.loadGameFromJSON( remoteGame, PRESEASON_FINISHED_REMOTE_GAME_JSON );

		assertEquals( remoteGameId, remoteGame.getRemoteGameId() );

		assertEquals( "1610612748", remoteGame.getRemoteTeam1Id() );
		assertEquals( "Miami", remoteGame.getRemoteTeam1Name() );

		assertEquals( "1610612740", remoteGame.getRemoteTeam2Id() );
		assertEquals( "New Orleans", remoteGame.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2014-10-04T15:00" ), remoteGame.getBeginningTime() );
		assertEquals( 1, remoteGame.getHomeTeamNumber() );

		assertEquals( 86, remoteGame.getScore1() );
		assertEquals( 98, remoteGame.getScore2() );

		assertTrue( remoteGame.isFinished() );
		assertFalse( remoteGame.isLoaded() );
	}

	@Test
	public void parsePreseasonFutureGame() throws IOException {

		final String remoteGameId = "0011500006";

		final NBAGameParsingService nbaGameParsingService = new NBAGameParsingService();
		nbaGameParsingService.setDateTimeService( new DateTimeServiceImpl() );

		final RemoteGame remoteGame = new RemoteGame( remoteGameId );
		nbaGameParsingService.loadGameFromJSON( remoteGame, PRESEASON_FUTURE_REMOTE_GAME_JSON );

		assertEquals( remoteGameId, remoteGame.getRemoteGameId() );

		assertEquals( "1610612766", remoteGame.getRemoteTeam1Id() );
		assertEquals( "Charlotte", remoteGame.getRemoteTeam1Name() );

		assertEquals( "1610612748", remoteGame.getRemoteTeam2Id() );
		assertEquals( "Miami", remoteGame.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-10-04T15:00" ), remoteGame.getBeginningTime() );
		assertEquals( 2, remoteGame.getHomeTeamNumber() );

		assertEquals( 0, remoteGame.getScore1() );
		assertEquals( 0, remoteGame.getScore2() );

		assertFalse( remoteGame.isFinished() );

		assertFalse( remoteGame.isLoaded() );
	}

	private boolean doesGameIdPresent( final Set<RemoteGame> remoteGameIds, final String remoteGameId ) {

		for ( final RemoteGame gameId : remoteGameIds ) {
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

	private static final String PRESEASON_REMOTE_GAMES_IDS_JSON = "{\"resource\":\"scoreboard\",\"parameters\":{\"GameDate\":\"10/4/2015\",\"LeagueID\":\"00\",\"DayOffset\":\"0\"},\"resultSets\":[{\"name\":\"GameHeader\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"GAME_STATUS_ID\",\"GAME_STATUS_TEXT\",\"GAMECODE\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"SEASON\",\"LIVE_PERIOD\",\"LIVE_PC_TIME\",\"NATL_TV_BROADCASTER_ABBREVIATION\",\"LIVE_PERIOD_TIME_BCAST\",\"WH_STATUS\"],\"rowSet\":[[\"2015-10-04T00:00:00\",1,\"0011500006\",1,\"6:00 pm ET\",\"20151004/CHAMIA\",1610612748,1610612766,\"2015\",0,\"     \",null,\"Q0       - \",0],[\"2015-10-04T00:00:00\",2,\"0011500005\",1,\"7:00 pm ET\",\"20151004/LACTOR\",1610612761,1610612746,\"2015\",0,\"     \",null,\"Q0       - \",0],[\"2015-10-04T00:00:00\",3,\"0011500004\",1,\"9:00 pm ET\",\"20151004/UTALAL\",1610612747,1610612762,\"2015\",0,\"     \",null,\"Q0       - \",0]]},{\"name\":\"LineScore\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY_NAME\",\"TEAM_WINS_LOSSES\",\"PTS_QTR1\",\"PTS_QTR2\",\"PTS_QTR3\",\"PTS_QTR4\",\"PTS_OT1\",\"PTS_OT2\",\"PTS_OT3\",\"PTS_OT4\",\"PTS_OT5\",\"PTS_OT6\",\"PTS_OT7\",\"PTS_OT8\",\"PTS_OT9\",\"PTS_OT10\",\"PTS\",\"FG_PCT\",\"FT_PCT\",\"FG3_PCT\",\"AST\",\"REB\",\"TOV\"],\"rowSet\":[[\"2015-10-04T00:00:00\",1,\"0011500006\",1610612748,\"MIA\",\"Miami\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",1,\"0011500006\",1610612766,\"CHA\",\"Charlotte\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",2,\"0011500005\",1610612761,\"TOR\",\"Toronto\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",2,\"0011500005\",1610612746,\"LAC\",\"Los Angeles\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",3,\"0011500004\",1610612747,\"LAL\",\"Los Angeles\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",3,\"0011500004\",1610612762,\"UTA\",\"Utah\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]]},{\"name\":\"SeriesStandings\",\"headers\":[\"GAME_ID\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"GAME_DATE_EST\",\"HOME_TEAM_WINS\",\"HOME_TEAM_LOSSES\",\"SERIES_LEADER\"],\"rowSet\":[]},{\"name\":\"LastMeeting\",\"headers\":[\"GAME_ID\",\"LAST_GAME_ID\",\"LAST_GAME_DATE_EST\",\"LAST_GAME_HOME_TEAM_ID\",\"LAST_GAME_HOME_TEAM_CITY\",\"LAST_GAME_HOME_TEAM_NAME\",\"LAST_GAME_HOME_TEAM_ABBREVIATION\",\"LAST_GAME_HOME_TEAM_POINTS\",\"LAST_GAME_VISITOR_TEAM_ID\",\"LAST_GAME_VISITOR_TEAM_CITY\",\"LAST_GAME_VISITOR_TEAM_NAME\",\"LAST_GAME_VISITOR_TEAM_CITY1\",\"LAST_GAME_VISITOR_TEAM_POINTS\"],\"rowSet\":[[\"0011500004\",\"0021401019\",\"2015-03-19T00:00:00\",1610612747,\"Los Angeles\",\"Lakers\",\"LAL\",73,1610612762,\"Utah\",\"Jazz\",\"UTA\",80],[\"0011500005\",\"0021400752\",\"2015-02-06T00:00:00\",1610612761,\"Toronto\",\"Raptors\",\"TOR\",123,1610612746,\"Los Angeles\",\"Clippers\",\"LAC\",107],[\"0011500006\",\"0021401155\",\"2015-04-07T00:00:00\",1610612748,\"Miami\",\"Heat\",\"MIA\",105,1610612766,\"Charlotte\",\"Hornets\",\"CHA\",100]]},{\"name\":\"EastConfStandingsByDay\",\"headers\":[\"TEAM_ID\",\"LEAGUE_ID\",\"SEASON_ID\",\"STANDINGSDATE\",\"CONFERENCE\",\"TEAM\",\"G\",\"W\",\"L\",\"W_PCT\",\"HOME_RECORD\",\"ROAD_RECORD\"],\"rowSet\":[[1610612737,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Atlanta\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612738,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Boston\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612751,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Brooklyn\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612766,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Charlotte\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612741,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Chicago\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612739,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Cleveland\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612765,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Detroit\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612754,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Indiana\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612748,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Miami\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612749,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Milwaukee\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612752,\"00\",\"12015\",\"10/04/2015\",\"East\",\"New York\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612753,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Orlando\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612755,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Philadelphia\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612761,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Toronto\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612764,\"00\",\"12015\",\"10/04/2015\",\"East\",\"Washington\",0,0,0,0.0,\"0-0\",\"0-0\"]]},{\"name\":\"WestConfStandingsByDay\",\"headers\":[\"TEAM_ID\",\"LEAGUE_ID\",\"SEASON_ID\",\"STANDINGSDATE\",\"CONFERENCE\",\"TEAM\",\"G\",\"W\",\"L\",\"W_PCT\",\"HOME_RECORD\",\"ROAD_RECORD\"],\"rowSet\":[[1610612742,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Dallas\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612743,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Denver\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612744,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Golden State\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612745,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Houston\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612746,\"00\",\"12015\",\"10/04/2015\",\"West\",\"L.A. Clippers\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612747,\"00\",\"12015\",\"10/04/2015\",\"West\",\"L.A. Lakers\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612763,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Memphis\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612750,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Minnesota\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612740,\"00\",\"12015\",\"10/04/2015\",\"West\",\"New Orleans\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612760,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Oklahoma City\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612756,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Phoenix\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612757,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Portland\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612758,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Sacramento\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612759,\"00\",\"12015\",\"10/04/2015\",\"West\",\"San Antonio\",0,0,0,0.0,\"0-0\",\"0-0\"],[1610612762,\"00\",\"12015\",\"10/04/2015\",\"West\",\"Utah\",0,0,0,0.0,\"0-0\",\"0-0\"]]},{\"name\":\"Available\",\"headers\":[\"GAME_ID\",\"PT_AVAILABLE\"],\"rowSet\":[[\"0011500004\",0],[\"0011500005\",0],[\"0011500006\",0]]}]}";


	private static final String PRESEASON_FINISHED_REMOTE_GAME_JSON = "{\"resource\":\"boxscore\",\"parameters\":{\"GameID\":\"0011400001\",\"StartPeriod\":0,\"EndPeriod\":0,\"StartRange\":0,\"EndRange\":0,\"RangeType\":0},\"resultSets\":[{\"name\":\"GameSummary\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"GAME_STATUS_ID\",\"GAME_STATUS_TEXT\",\"GAMECODE\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"SEASON\",\"LIVE_PERIOD\",\"LIVE_PC_TIME\",\"NATL_TV_BROADCASTER_ABBREVIATION\",\"LIVE_PERIOD_TIME_BCAST\",\"WH_STATUS\"],\"rowSet\":[[\"2014-10-04T00:00:00\",1,\"0011400001\",3,\"Final\",\"20141004/NOPMIA\",1610612748,1610612740,\"2014\",4,\" \",null,\"Q4   - \",1]]},{\"name\":\"LineScore\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY_NAME\",\"TEAM_WINS_LOSSES\",\"PTS_QTR1\",\"PTS_QTR2\",\"PTS_QTR3\",\"PTS_QTR4\",\"PTS_OT1\",\"PTS_OT2\",\"PTS_OT3\",\"PTS_OT4\",\"PTS_OT5\",\"PTS_OT6\",\"PTS_OT7\",\"PTS_OT8\",\"PTS_OT9\",\"PTS_OT10\",\"PTS\"],\"rowSet\":[[\"2014-10-04T00:00:00\",1,\"0011400001\",1610612748,\"MIA\",\"Miami\",\"0-1\",18,17,26,25,0,0,0,0,0,0,0,0,0,0,86],[\"2014-10-04T00:00:00\",1,\"0011400001\",1610612740,\"NOP\",\"New Orleans\",\"1-0\",16,28,23,31,0,0,0,0,0,0,0,0,0,0,98]]},{\"name\":\"SeasonSeries\",\"headers\":[\"GAME_ID\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"GAME_DATE_EST\",\"HOME_TEAM_WINS\",\"HOME_TEAM_LOSSES\",\"SERIES_LEADER\"],\"rowSet\":[[\"0011400001\",1610612748,1610612740,\"2014-10-04T00:00:00\",0,1,\"New Orleans\"]]},{\"name\":\"LastMeeting\",\"headers\":[\"GAME_ID\",\"LAST_GAME_ID\",\"LAST_GAME_DATE_EST\",\"LAST_GAME_HOME_TEAM_ID\",\"LAST_GAME_HOME_TEAM_CITY\",\"LAST_GAME_HOME_TEAM_NAME\",\"LAST_GAME_HOME_TEAM_ABBREVIATION\",\"LAST_GAME_HOME_TEAM_POINTS\",\"LAST_GAME_VISITOR_TEAM_ID\",\"LAST_GAME_VISITOR_TEAM_CITY\",\"LAST_GAME_VISITOR_TEAM_NAME\",\"LAST_GAME_VISITOR_TEAM_CITY1\",\"LAST_GAME_VISITOR_TEAM_POINTS\"],\"rowSet\":[]},{\"name\":\"PlayerStats\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PLAYER_ID\",\"PLAYER_NAME\",\"START_POSITION\",\"COMMENT\",\"MIN\",\"FGM\",\"FGA\",\"FG_PCT\",\"FG3M\",\"FG3A\",\"FG3_PCT\",\"FTM\",\"FTA\",\"FT_PCT\",\"OREB\",\"DREB\",\"REB\",\"AST\",\"STL\",\"BLK\",\"TO\",\"PF\",\"PTS\",\"PLUS_MINUS\"],\"rowSet\":[[\"0011400001\",1610612748,\"MIA\",\"Miami\",2547,\"Chris Bosh\",\"\",\"\",25,3,13,0.231,1,3,0.333,2,3,0.667,1,5,6,2,1,0,4,2,9,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",202419,\"Chris Johnson\",\"\",\"\",6,0,0,0.000,0,0,0.000,0,0,0.000,0,1,1,1,0,1,0,1,0,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",101122,\"Danny Granger\",\"\",\"\",22,5,8,0.625,2,4,0.500,0,1,0.000,0,2,2,4,0,0,1,3,12,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2548,\"Dwyane Wade\",\"\",\"\",21,2,7,0.286,0,1,0.000,2,4,0.500,0,2,2,3,0,0,1,1,6,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203516,\"James Ennis\",\"\",\"\",23,4,8,0.500,1,2,0.500,8,10,0.800,0,6,6,1,1,0,0,0,17,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203920,\"Khem Birch\",\"\",\"\",17,2,3,0.667,0,0,0.000,0,2,0.000,3,1,4,1,1,1,2,2,4,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2736,\"Luol Deng\",\"\",\"\",25,2,7,0.286,0,1,0.000,0,0,0.000,2,1,3,1,2,0,0,0,4,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",201596,\"Mario Chalmers\",\"\",\"\",21,0,2,0.000,0,1,0.000,2,2,1.000,0,3,3,2,1,0,2,1,2,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",202708,\"Norris Cole\",\"\",\"\",24,4,8,0.500,0,1,0.000,2,2,1.000,1,2,3,2,4,0,0,0,10,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203894,\"Shabazz Napier\",\"\",\"\",8,2,3,0.667,1,1,1.000,0,0,0.000,1,0,1,0,0,0,1,2,5,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",200769,\"Shannon Brown\",\"\",\"\",6,0,1,0.000,0,0,0.000,1,2,0.500,0,0,0,0,0,0,1,0,1,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",204053,\"Shawn Jones\",\"\",\"\",15,1,3,0.333,0,0,0.000,0,0,0.000,2,4,6,0,0,0,2,2,2,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",200761,\"Shawne Williams\",\"\",\"\",17,5,7,0.714,0,0,0.000,2,2,1.000,0,3,3,0,0,1,1,1,12,0],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2617,\"Udonis Haslem\",\"\",\"\",11,1,2,0.500,0,1,0.000,0,0,0.000,0,2,2,1,1,0,0,2,2,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203076,\"Anthony Davis\",\"\",\"\",11,2,4,0.500,0,0,0.000,1,2,0.500,1,3,4,0,0,3,1,0,5,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203085,\"Austin Rivers\",\"\",\"\",32,5,8,0.625,1,2,0.500,1,3,0.333,0,0,0,6,0,0,2,4,12,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203474,\"DJ Stephens\",\"\",\"\",3,1,1,1.000,0,0,0.000,0,0,0.000,0,0,0,0,0,0,0,1,2,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203121,\"Darius Miller\",\"\",\"\",32,4,12,0.333,0,5,0.000,0,0,0.000,0,3,3,2,1,0,1,3,8,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201569,\"Eric Gordon\",\"\",\"\",10,0,4,0.000,0,2,0.000,4,4,1.000,0,1,1,2,0,0,1,0,4,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203481,\"Jeff Withey\",\"\",\"\",19,2,6,0.333,0,0,0.000,0,0,0.000,2,5,7,0,0,0,3,6,4,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202690,\"Jimmer Fredette\",\"\",\"\",30,6,9,0.667,3,4,0.750,2,2,1.000,0,1,1,3,2,0,2,0,17,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",2422,\"John Salmons\",\"\",\"\",6,1,2,0.500,1,1,1.000,0,0,0.000,0,1,1,0,0,0,0,1,3,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201950,\"Jrue Holiday\",\"\",\"\",10,0,2,0.000,0,1,0.000,0,0,0.000,0,2,2,0,0,0,1,2,0,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203158,\"Kevin Jones\",\"\",\"\",4,0,0,0.000,0,0,0.000,0,0,0.000,0,1,1,0,0,0,0,0,0,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202337,\"Luke Babbitt\",\"\",\"\",27,5,11,0.455,5,10,0.500,0,0,0.000,1,5,6,2,1,0,1,2,15,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201600,\"Omer Asik\",\"\",\"\",6,1,2,0.500,0,0,0.000,0,0,0.000,0,3,3,1,0,1,1,2,2,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203942,\"Patric Young\",\"\",\"\",21,1,1,1.000,0,0,0.000,0,0,0.000,3,5,8,0,0,1,2,2,2,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203893,\"Russ Smith\",\"\",\"\",13,4,7,0.571,1,1,1.000,3,4,0.750,2,1,3,1,2,0,2,1,12,0],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201583,\"Ryan Anderson\",\"\",\"\",14,4,9,0.444,3,6,0.500,1,2,0.500,1,1,2,2,0,1,2,1,12,0]]},{\"name\":\"TeamStats\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_NAME\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"MIN\",\"FGM\",\"FGA\",\"FG_PCT\",\"FG3M\",\"FG3A\",\"FG3_PCT\",\"FTM\",\"FTA\",\"FT_PCT\",\"OREB\",\"DREB\",\"REB\",\"AST\",\"STL\",\"BLK\",\"TO\",\"PF\",\"PTS\",\"PLUS_MINUS\"],\"rowSet\":[[\"0011400001\",1610612748,\"Heat\",\"MIA\",\"Miami\",241,31,72,0.431,5,15,0.333,19,28,0.679,10,32,42,18,11,3,15,17,86,0],[\"0011400001\",1610612740,\"Pelicans\",\"NOP\",\"New Orleans\",238,36,78,0.462,14,32,0.438,12,17,0.706,10,32,42,19,6,6,19,25,98,0]]},{\"name\":\"OtherStats\",\"headers\":[\"LEAGUE_ID\",\"SEASON_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PTS_PAINT\",\"PTS_2ND_CHANCE\",\"PTS_FB\",\"LARGEST_LEAD\",\"LEAD_CHANGES\",\"TIMES_TIED\"],\"rowSet\":[[\"00\",\"12014\",1610612740,\"NOP\",\"New Orleans\",36,13,12,12,4,5],[\"00\",\"12014\",1610612748,\"MIA\",\"Miami\",36,7,18,12,4,5]]},{\"name\":\"Officials\",\"headers\":[\"OFFICIAL_ID\",\"FIRST_NAME\",\"LAST_NAME\",\"JERSEY_NUM\"],\"rowSet\":[[1190,\"Eddie\",\"Rush\",\"32  \"],[2534,\"Zach\",\"Zarba\",\"28  \"],[101283,\"Brian\",\"Forte\",\"45  \"]]},{\"name\":\"GameInfo\",\"headers\":[\"GAME_DATE\",\"ATTENDANCE\",\"GAME_TIME\"],\"rowSet\":[[\"SATURDAY, OCTOBER 4, 2014\",20074,\"\"]]},{\"name\":\"InactivePlayers\",\"headers\":[\"PLAYER_ID\",\"FIRST_NAME\",\"LAST_NAME\",\"JERSEY_NUM\",\"TEAM_ID\",\"TEAM_CITY\",\"TEAM_NAME\",\"TEAM_ABBREVIATION\"],\"rowSet\":[]},{\"name\":\"AvailableVideo\",\"headers\":[\"GAME_ID\",\"VIDEO_AVAILABLE_FLAG\",\"PT_AVAILABLE\"],\"rowSet\":[[\"0011400001\",0,0]]},{\"name\":\"PlayerTrack\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PLAYER_ID\",\"PLAYER_NAME\",\"START_POSITION\",\"COMMENT\",\"MIN\",\"SPD\",\"DIST\",\"ORBC\",\"DRBC\",\"RBC\",\"TCHS\",\"SAST\",\"FTAST\",\"PASS\",\"AST\",\"CFGM\",\"CFGA\",\"CFG_PCT\",\"UFGM\",\"UFGA\",\"UFG_PCT\",\"FG_PCT\",\"DFGM\",\"DFGA\",\"DFG_PCT\"],\"rowSet\":[[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203121,\"Darius Miller\",\"F\",\"\",\"32:23\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.333,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203076,\"Anthony Davis\",\"F\",\"\",\"11:20\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201600,\"Omer Asik\",\"C\",\"\",\"6:04\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201569,\"Eric Gordon\",\"G\",\"\",\"10:07\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201950,\"Jrue Holiday\",\"G\",\"\",\"10:07\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201583,\"Ryan Anderson\",\"\",\"\",\"14:04\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.444,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",2422,\"John Salmons\",\"\",\"\",\"6:03\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203085,\"Austin Rivers\",\"\",\"\",\"32:24\",0.0,0.0,0,0,0,0,0,0,0,6,0,0,0.000,0,0,0.000,0.625,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202690,\"Jimmer Fredette\",\"\",\"\",\"30:04\",0.0,0.0,0,0,0,0,0,0,0,3,0,0,0.000,0,0,0.000,0.667,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203481,\"Jeff Withey\",\"\",\"\",\"19:21\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.333,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202337,\"Luke Babbitt\",\"\",\"\",\"27:26\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.455,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203942,\"Patric Young\",\"\",\"\",\"20:39\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,1.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203893,\"Russ Smith\",\"\",\"\",\"13:18\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.571,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203158,\"Kevin Jones\",\"\",\"\",\"4:03\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",203474,\"DJ Stephens\",\"\",\"\",\"2:37\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,1.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201582,\"Alexis Ajinca\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202091,\"Dionte Christmas\",\"\",\"NWT - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",201936,\"Tyreke Evans\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612740,\"NOP\",\"New Orleans\",202731,\"Vernon Macklin\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2736,\"Luol Deng\",\"F\",\"\",\"24:50\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.286,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2617,\"Udonis Haslem\",\"F\",\"\",\"10:32\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2547,\"Chris Bosh\",\"C\",\"\",\"25:20\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.231,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2548,\"Dwyane Wade\",\"G\",\"\",\"20:31\",0.0,0.0,0,0,0,0,0,0,0,3,0,0,0.000,0,0,0.000,0.286,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",201596,\"Mario Chalmers\",\"G\",\"\",\"21:16\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",200761,\"Shawne Williams\",\"\",\"\",\"17:24\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.714,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",202708,\"Norris Cole\",\"\",\"\",\"23:33\",0.0,0.0,0,0,0,0,0,0,0,2,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203516,\"James Ennis\",\"\",\"\",\"23:10\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.500,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",101122,\"Danny Granger\",\"\",\"\",\"22:02\",0.0,0.0,0,0,0,0,0,0,0,4,0,0,0.000,0,0,0.000,0.625,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203920,\"Khem Birch\",\"\",\"\",\"16:47\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.667,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",204053,\"Shawn Jones\",\"\",\"\",\"15:19\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.333,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203894,\"Shabazz Napier\",\"\",\"\",\"7:30\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.667,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",200769,\"Shannon Brown\",\"\",\"\",\"5:53\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",202419,\"Chris Johnson\",\"\",\"\",\"5:53\",0.0,0.0,0,0,0,0,0,0,0,1,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",2365,\"Chris Andersen\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203958,\"Andre Dawkins\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",203120,\"Justin Hamilton\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",204020,\"Tyler Johnson\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",201177,\"Josh McRoberts\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000],[\"0011400001\",1610612748,\"MIA\",\"Miami\",202130,\"Reggie Williams\",\"\",\"DNP - Coach's Decision                  \",\"0:00\",0.0,0.0,0,0,0,0,0,0,0,0,0,0,0.000,0,0,0.000,0.000,0,0,0.000]]},{\"name\":\"PlayerTrackTeam\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_NICKNAME\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"MIN\",\"DIST\",\"ORBC\",\"DRBC\",\"RBC\",\"TCHS\",\"SAST\",\"FTAST\",\"PASS\",\"AST\",\"CFGM\",\"CFGA\",\"CFG_PCT\",\"UFGM\",\"UFGA\",\"UFG_PCT\",\"FG_PCT\",\"DFGM\",\"DFGA\",\"DFG_PCT\"],\"rowSet\":[[\"0011400001\",1610612740,\"Pelicans\",\"NOP\",\"New Orleans\",\"240:00\",0.0,0,0,0,0,0,0,0,19,0,0,0.000,0,0,0.000,0.462,0,0,0.000],[\"0011400001\",1610612748,\"Heat\",\"MIA\",\"Miami\",\"240:00\",0.0,0,0,0,0,0,0,0,18,0,0,0.000,0,0,0.000,0.431,0,0,0.000]]}]}";

	private static final String PRESEASON_FUTURE_REMOTE_GAME_JSON = "{\"resource\":\"boxscore\",\"parameters\":{\"GameID\":\"0011500006\",\"StartPeriod\":0,\"EndPeriod\":0,\"StartRange\":0,\"EndRange\":0,\"RangeType\":0},\"resultSets\":[{\"name\":\"GameSummary\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"GAME_STATUS_ID\",\"GAME_STATUS_TEXT\",\"GAMECODE\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"SEASON\",\"LIVE_PERIOD\",\"LIVE_PC_TIME\",\"NATL_TV_BROADCASTER_ABBREVIATION\",\"LIVE_PERIOD_TIME_BCAST\",\"WH_STATUS\"],\"rowSet\":[[\"2015-10-04T00:00:00\",1,\"0011500006\",1,\"6:00 pm ET\",\"20151004/CHAMIA\",1610612748,1610612766,\"2015\",0,\"     \",null,\"Q0       - \",1]]},{\"name\":\"LineScore\",\"headers\":[\"GAME_DATE_EST\",\"GAME_SEQUENCE\",\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY_NAME\",\"TEAM_WINS_LOSSES\",\"PTS_QTR1\",\"PTS_QTR2\",\"PTS_QTR3\",\"PTS_QTR4\",\"PTS_OT1\",\"PTS_OT2\",\"PTS_OT3\",\"PTS_OT4\",\"PTS_OT5\",\"PTS_OT6\",\"PTS_OT7\",\"PTS_OT8\",\"PTS_OT9\",\"PTS_OT10\",\"PTS\"],\"rowSet\":[[\"2015-10-04T00:00:00\",1,\"0011500006\",1610612766,\"CHA\",\"Charlotte\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[\"2015-10-04T00:00:00\",1,\"0011500006\",1610612748,\"MIA\",\"Miami\",\"-\",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]]},{\"name\":\"SeasonSeries\",\"headers\":[\"GAME_ID\",\"HOME_TEAM_ID\",\"VISITOR_TEAM_ID\",\"GAME_DATE_EST\",\"HOME_TEAM_WINS\",\"HOME_TEAM_LOSSES\",\"SERIES_LEADER\"],\"rowSet\":[]},{\"name\":\"LastMeeting\",\"headers\":[\"GAME_ID\",\"LAST_GAME_ID\",\"LAST_GAME_DATE_EST\",\"LAST_GAME_HOME_TEAM_ID\",\"LAST_GAME_HOME_TEAM_CITY\",\"LAST_GAME_HOME_TEAM_NAME\",\"LAST_GAME_HOME_TEAM_ABBREVIATION\",\"LAST_GAME_HOME_TEAM_POINTS\",\"LAST_GAME_VISITOR_TEAM_ID\",\"LAST_GAME_VISITOR_TEAM_CITY\",\"LAST_GAME_VISITOR_TEAM_NAME\",\"LAST_GAME_VISITOR_TEAM_CITY1\",\"LAST_GAME_VISITOR_TEAM_POINTS\"],\"rowSet\":[]},{\"name\":\"PlayerStats\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PLAYER_ID\",\"PLAYER_NAME\",\"START_POSITION\",\"COMMENT\",\"MIN\",\"FGM\",\"FGA\",\"FG_PCT\",\"FG3M\",\"FG3A\",\"FG3_PCT\",\"FTM\",\"FTA\",\"FT_PCT\",\"OREB\",\"DREB\",\"REB\",\"AST\",\"STL\",\"BLK\",\"TO\",\"PF\",\"PTS\",\"PLUS_MINUS\"],\"rowSet\":[]},{\"name\":\"TeamStats\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_NAME\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"MIN\",\"FGM\",\"FGA\",\"FG_PCT\",\"FG3M\",\"FG3A\",\"FG3_PCT\",\"FTM\",\"FTA\",\"FT_PCT\",\"OREB\",\"DREB\",\"REB\",\"AST\",\"STL\",\"BLK\",\"TO\",\"PF\",\"PTS\",\"PLUS_MINUS\"],\"rowSet\":[]},{\"name\":\"OtherStats\",\"headers\":[\"LEAGUE_ID\",\"SEASON_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PTS_PAINT\",\"PTS_2ND_CHANCE\",\"PTS_FB\",\"LARGEST_LEAD\",\"LEAD_CHANGES\",\"TIMES_TIED\"],\"rowSet\":[]},{\"name\":\"Officials\",\"headers\":[\"OFFICIAL_ID\",\"FIRST_NAME\",\"LAST_NAME\",\"JERSEY_NUM\"],\"rowSet\":[]},{\"name\":\"GameInfo\",\"headers\":[\"GAME_DATE\",\"ATTENDANCE\",\"GAME_TIME\"],\"rowSet\":[[\"SUNDAY, OCTOBER 4, 2015\",null,\"\"]]},{\"name\":\"InactivePlayers\",\"headers\":[\"PLAYER_ID\",\"FIRST_NAME\",\"LAST_NAME\",\"JERSEY_NUM\",\"TEAM_ID\",\"TEAM_CITY\",\"TEAM_NAME\",\"TEAM_ABBREVIATION\"],\"rowSet\":[]},{\"name\":\"AvailableVideo\",\"headers\":[\"GAME_ID\",\"VIDEO_AVAILABLE_FLAG\",\"PT_AVAILABLE\"],\"rowSet\":[[\"0011500006\",0,0]]},{\"name\":\"PlayerTrack\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"PLAYER_ID\",\"PLAYER_NAME\",\"START_POSITION\",\"COMMENT\",\"MIN\",\"SPD\",\"DIST\",\"ORBC\",\"DRBC\",\"RBC\",\"TCHS\",\"SAST\",\"FTAST\",\"PASS\",\"AST\",\"CFGM\",\"CFGA\",\"CFG_PCT\",\"UFGM\",\"UFGA\",\"UFG_PCT\",\"FG_PCT\",\"DFGM\",\"DFGA\",\"DFG_PCT\"],\"rowSet\":[]},{\"name\":\"PlayerTrackTeam\",\"headers\":[\"GAME_ID\",\"TEAM_ID\",\"TEAM_NICKNAME\",\"TEAM_ABBREVIATION\",\"TEAM_CITY\",\"MIN\",\"DIST\",\"ORBC\",\"DRBC\",\"RBC\",\"TCHS\",\"SAST\",\"FTAST\",\"PASS\",\"AST\",\"CFGM\",\"CFGA\",\"CFG_PCT\",\"UFGM\",\"UFGA\",\"UFG_PCT\",\"FG_PCT\",\"DFGM\",\"DFGA\",\"DFG_PCT\"],\"rowSet\":[]}]}";
}