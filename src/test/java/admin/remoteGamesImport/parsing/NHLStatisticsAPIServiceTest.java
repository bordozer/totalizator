package admin.remoteGamesImport.parsing;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.services.TeamService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.strategies.nhl.NHLStatisticsAPIService;
import totalizator.app.services.matches.imports.strategies.nhl.NHLStatisticsServerURLService;
import totalizator.app.services.remote.RemoteContentNullException;
import totalizator.app.services.remote.RemoteContentService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.*;

public class NHLStatisticsAPIServiceTest {

	private static final List<String> EXPECTED_FUTURE_REMOTE_GAME_IDS = newArrayList( "2015010017", "2015010030", "2015010042", "2015010050" );

	private static final String TEAM_1_GAMES_JSON_URL = "http://nhlwc.cdnak.neulion.com/fs1/nhl/league/clubschedule/CHI/2015/9/iphone/clubschedule.json";
	private static final String TEAM_2_GAMES_JSON_URL = "http://nhlwc.cdnak.neulion.com/fs1/nhl/league/clubschedule/EDM/2015/9/iphone/clubschedule.json";

	private TestData testData;

	@Before
	public void setup() {
		testData = new TestData();
	}

	@Test
	public void extractRemoteGameIds() throws IOException, RemoteContentNullException {

		final NHLStatisticsAPIService statisticsAPIService = getNhlStatisticsAPIService( TEAM_1_FUTURE_GAMES_JSON, TEAM_2_FUTURE_GAMES_JSON );

		final Set<RemoteGame> remoteGames = statisticsAPIService.preloadRemoteGames( testData.cup, testData.date, testData.date );

		assertEquals( remoteGames.size(), EXPECTED_FUTURE_REMOTE_GAME_IDS.size() );

		for ( final String remoteGameId : EXPECTED_FUTURE_REMOTE_GAME_IDS ) {
			assertTrue( doesGameIdPresent( remoteGames, remoteGameId ) );
		}

		int i = 0;
		for ( final RemoteGame remoteGame : remoteGames ) {
			assertEquals( EXPECTED_FUTURE_REMOTE_GAME_IDS.get( i ), remoteGame.getRemoteGameId() );

			i++;
		}
	}

	@Test
	public void pastGames() throws IOException, RemoteContentNullException {

		final NHLStatisticsAPIService statisticsAPIService = getNhlStatisticsAPIService( TEAM_1_PAST_GAMES_JSON, TEAM_2_PAST_GAMES_JSON );

		final List<RemoteGame> remoteGames = newArrayList( statisticsAPIService.preloadRemoteGames( testData.cup, testData.date, testData.date ) );

		assertEquals( 11, remoteGames.size() );

		final RemoteGame remoteGame1 = remoteGames.get( 0 );

		assertEquals( "2014030231", remoteGame1.getRemoteGameId() );

		assertEquals( "CHI", remoteGame1.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame1.getRemoteTeam1Name() );

		assertEquals( "MIN", remoteGame1.getRemoteTeam2Id() );
		assertEquals( "MIN", remoteGame1.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-05-01T21:30" ), remoteGame1.getBeginningTime() );
		assertEquals( 1, remoteGame1.getHomeTeamNumber() );

		assertEquals( 3, remoteGame1.getScore1() );
		assertEquals( 4, remoteGame1.getScore2() );

		assertTrue( remoteGame1.isFinished() );
		assertTrue( remoteGame1.isLoaded() );



		final RemoteGame remoteGame2 = remoteGames.get( 1 );

		assertEquals( "2014030232", remoteGame2.getRemoteGameId() );

		assertEquals( "CHI", remoteGame2.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame2.getRemoteTeam1Name() );

		assertEquals( "MIN", remoteGame2.getRemoteTeam2Id() );
		assertEquals( "MIN", remoteGame2.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-05-03T20:30" ), remoteGame2.getBeginningTime() );
		assertEquals( 1, remoteGame2.getHomeTeamNumber() );

		assertEquals( 1, remoteGame2.getScore1() );
		assertEquals( 4, remoteGame2.getScore2() );

		assertTrue( remoteGame2.isFinished() );
		assertTrue( remoteGame2.isLoaded() );



		final RemoteGame remoteGame3 = remoteGames.get( 7 );

		assertEquals( "2014030324", remoteGame3.getRemoteGameId() );

		assertEquals( "CHI", remoteGame3.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame3.getRemoteTeam1Name() );

		assertEquals( "ANA", remoteGame3.getRemoteTeam2Id() );
		assertEquals( "ANA", remoteGame3.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-05-23T20:00" ), remoteGame3.getBeginningTime() );
		assertEquals( 1, remoteGame3.getHomeTeamNumber() );

		assertEquals( 4, remoteGame3.getScore1() );
		assertEquals( 5, remoteGame3.getScore2() );

		assertTrue( remoteGame3.isFinished() );
		assertTrue( remoteGame3.isLoaded() );
	}

	@Test
	public void futureGames() throws IOException, RemoteContentNullException {

		final NHLStatisticsAPIService statisticsAPIService = getNhlStatisticsAPIService( TEAM_1_FUTURE_GAMES_JSON, TEAM_2_FUTURE_GAMES_JSON );

		final List<RemoteGame> remoteGames = newArrayList( statisticsAPIService.preloadRemoteGames( testData.cup, testData.date, testData.date ) );

		assertEquals( 4, remoteGames.size() );

		final RemoteGame remoteGame1 = remoteGames.get( 0 );

		assertEquals( "2015010017", remoteGame1.getRemoteGameId() );

		assertEquals( "CHI", remoteGame1.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame1.getRemoteTeam1Name() );

		assertEquals( "DET", remoteGame1.getRemoteTeam2Id() );
		assertEquals( "DET", remoteGame1.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-09-22T20:30" ), remoteGame1.getBeginningTime() );
		assertEquals( 1, remoteGame1.getHomeTeamNumber() );

		assertEquals( 0, remoteGame1.getScore1() );
		assertEquals( 0, remoteGame1.getScore2() );

		assertFalse( remoteGame1.isFinished() );
		assertTrue( remoteGame1.isLoaded() );



		final RemoteGame remoteGame2 = remoteGames.get( 1 );

		assertEquals( "2015010030", remoteGame2.getRemoteGameId() );

		assertEquals( "CHI", remoteGame2.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame2.getRemoteTeam1Name() );

		assertEquals( "DET", remoteGame2.getRemoteTeam2Id() );
		assertEquals( "DET", remoteGame2.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-09-23T19:30" ), remoteGame2.getBeginningTime() );
		assertEquals( 2, remoteGame2.getHomeTeamNumber() );

		assertEquals( 0, remoteGame2.getScore1() );
		assertEquals( 0, remoteGame2.getScore2() );

		assertFalse( remoteGame2.isFinished() );
		assertTrue( remoteGame2.isLoaded() );



		final RemoteGame remoteGame3 = remoteGames.get( 2 );

		assertEquals( "2015010042", remoteGame3.getRemoteGameId() );

		assertEquals( "CHI", remoteGame3.getRemoteTeam1Id() );
		assertEquals( "CHI", remoteGame3.getRemoteTeam1Name() );

		assertEquals( "MTL", remoteGame3.getRemoteTeam2Id() );
		assertEquals( "MTL", remoteGame3.getRemoteTeam2Name() );

		assertEquals( LocalDateTime.parse( "2015-09-25T19:30" ), remoteGame3.getBeginningTime() );
		assertEquals( 2, remoteGame3.getHomeTeamNumber() );

		assertEquals( 0, remoteGame3.getScore1() );
		assertEquals( 0, remoteGame3.getScore2() );

		assertFalse( remoteGame3.isFinished() );
		assertTrue( remoteGame3.isLoaded() );
	}

	private NHLStatisticsAPIService getNhlStatisticsAPIService( String team1GamesJson, String team2GamesJson ) throws IOException, RemoteContentNullException {

		final NHLStatisticsAPIService nbaGameParsingService = new NHLStatisticsAPIService();
		nbaGameParsingService.setNhlStatisticsServerURLService( getNhlStatisticsServerURLService( testData ) );
		nbaGameParsingService.setRemoteContentService( getRemoteContentService( testData, team1GamesJson, team2GamesJson ) );
		nbaGameParsingService.setTeamService( getTeamService( testData ) );

		return nbaGameParsingService;
	}

	private NHLStatisticsServerURLService getNhlStatisticsServerURLService( final TestData testData ) {

		final NHLStatisticsServerURLService nhlStatisticsServerURLService = EasyMock.createMock( NHLStatisticsServerURLService.class );

		EasyMock.expect( nhlStatisticsServerURLService.getURL( testData.team1, testData.date ) ).andReturn( TEAM_1_GAMES_JSON_URL ).anyTimes();
		EasyMock.expect( nhlStatisticsServerURLService.getURL( testData.team2, testData.date ) ).andReturn( TEAM_2_GAMES_JSON_URL ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( nhlStatisticsServerURLService );

		return nhlStatisticsServerURLService;
	}

	private RemoteContentService getRemoteContentService( final TestData testData, final String team1GamesJson, final String team2GamesJson ) throws IOException, RemoteContentNullException {

		final RemoteContentService remoteContentService = EasyMock.createMock( RemoteContentService.class );

		EasyMock.expect( remoteContentService.getRemoteContent( TEAM_1_GAMES_JSON_URL ) ).andReturn( team1GamesJson ).anyTimes();
		EasyMock.expect( remoteContentService.getRemoteContent( TEAM_2_GAMES_JSON_URL ) ).andReturn( team2GamesJson ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( remoteContentService );

		return remoteContentService;
	}

	private TeamService getTeamService( final TestData testData ) {

		final TeamService teamService = EasyMock.createMock( TeamService.class );

		EasyMock.expect( teamService.loadAll( testData.cup.getCategory() ) ).andReturn( newArrayList( testData.team1, testData.team2 ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( teamService );

		return teamService;
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

		private Team team1;
		private Team team2;

		private LocalDate date;

		private String team1GamesJson;
		private String team2GamesJson;

		public TestData() {

			final Category category = new Category();
			category.setId( 999 );

			cup = new Cup();
			cup.setId( 555 );
			cup.setCupImportId( "77777777" );
			cup.setCategory( category );

			team1 = new Team();
			team1.setImportId( "CHI" );
			team1.setCategory( category );

			team2 = new Team();
			team2.setImportId( "BUF" );
			team2.setCategory( category );

			date = LocalDate.of( 2015, 9, 21 );
		}
	}

	private static final String TEAM_1_PAST_GAMES_JSON = "{\"timestamp\":\"Sun May 31 23:59:56 EDT 2015\",\"games\":[{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-SN-TVA2-370x120.png\",\"abb\":\"MIN\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030231,\"score\":\"3-4 W\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/01 21:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-SN360-TVA2-370x120.png\",\"abb\":\"MIN\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030232,\"score\":\"1-4 W\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/03 20:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"MIN\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030233,\"score\":\"1-0 W\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/05 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-SN-TVA2-370x120.png\",\"abb\":\"MIN\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030234,\"score\":\"4-3 W\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/07 21:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030321,\"score\":\"1-4 L\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":true,\"usLogo\":\"\"},\"startTime\":\"2015/05/17 15:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBC-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"3OT\",\"status\":\"FINAL\",\"gameId\":2014030322,\"score\":\"3-2 W\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/19 21:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030323,\"score\":\"2-1 L\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/21 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"2OT\",\"status\":\"FINAL\",\"gameId\":2014030324,\"score\":\"4-5 W\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":true,\"usLogo\":\"\"},\"startTime\":\"2015/05/23 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBC-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"OT\",\"status\":\"FINAL\",\"gameId\":2014030325,\"score\":\"4-5 L / OT\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/25 21:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030326,\"score\":\"2-5 W\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/27 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"ANA\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030327,\"score\":\"5-3 W\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":true,\"usLogo\":\"\"},\"startTime\":\"2015/05/30 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBC-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}}]}\n";

	private static final String TEAM_2_PAST_GAMES_JSON = "{\"timestamp\":\"Sun May 31 23:59:57 EDT 2015\",\"games\":[{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-SN-TVA2-370x120.png\",\"abb\":\"CHI\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030231,\"score\":\"3-4 L\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/01 21:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-SN360-TVA2-370x120.png\",\"abb\":\"CHI\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030232,\"score\":\"1-4 L\",\"loc\":\"away\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/03 20:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-CBC-TVA-370x120.png\",\"abb\":\"CHI\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030233,\"score\":\"1-0 L\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/05 20:00:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-SN-TVA2-370x120.png\",\"abb\":\"CHI\",\"cPeriod\":\"3RD\",\"status\":\"FINAL\",\"gameId\":2014030234,\"score\":\"4-3 L\",\"loc\":\"home\",\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"startTime\":\"2015/05/07 21:30:00\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NBCSN-370x120.png\",\"aud\":{\"hasLiveHomeRadio\":false,\"hasLiveAwayRadio\":false,\"hasLiveFrenchRadio\":false},\"gs\":7,\"vid\":{\"hasArchiveHomeVideo\":true,\"hasLiveCam2Video\":false,\"hasLiveCam1Video\":false,\"hasArchiveAwayVideo\":true,\"hasCondensedVideo\":true,\"hasLiveHomeVideo\":false,\"hasLiveAwayVideo\":false,\"hasContinuousVideo\":true}}]}\n";

	private static final String TEAM_1_FUTURE_GAMES_JSON = "{\"timestamp\":\"Mon Sep 21 06:03:33 EDT 2015\",\"games\":[{\"startTime\":\"2015/09/22 20:30:00\",\"abb\":\"DET\",\"usTvLocalMsg\":\"Additionally, the game is being broadcast on WGN\",\"cPeriod\":\"\",\"gameId\":2015010017,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1},{\"startTime\":\"2015/09/23 19:30:00\",\"abb\":\"DET\",\"usTvLocalMsg\":\"Additionally, the game is being broadcast on FS-D and CSN-CH+\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NHLN-US-370x120.png\",\"cPeriod\":\"\",\"gameId\":2015010030,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"away\",\"gs\":1},{\"startTime\":\"2015/09/25 19:30:00\",\"abb\":\"MTL\",\"usTvLocalMsg\":\"Additionally, the game is being broadcast on CSN-CH+\",\"usTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-NHLN-US-370x120.png\",\"cPeriod\":\"\",\"gameId\":2015010042,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"away\",\"gs\":1},{\"startTime\":\"2015/09/26 20:30:00\",\"abb\":\"STL\",\"usTvLocalMsg\":\"Additionally, the game is being broadcast on WGN\",\"cPeriod\":\"\",\"gameId\":2015010050,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1}]}\n";

	private static final String TEAM_2_FUTURE_GAMES_JSON = "{\"timestamp\":\"Mon Sep 21 06:08:34 EDT 2015\",\"games\":[{\"startTime\":\"2015/09/21 21:00:00\",\"abb\":\"CGY\",\"cPeriod\":\"\",\"gameId\":2015010005,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"away\",\"gs\":1},{\"startTime\":\"2015/09/21 21:00:00\",\"abb\":\"CGY\",\"cPeriod\":\"\",\"gameId\":2015010007,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-TSN3-370x120.png\",\"startTime\":\"2015/09/23 21:00:00\",\"abb\":\"WPG\",\"cPeriod\":\"\",\"gameId\":2015010031,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1},{\"caTvNationalURL\":\"http://cdn.nhle.com/nhl/images/logos/broadcast/NHL-GC-Phone-TSN3-370x120.png\",\"startTime\":\"2015/09/25 20:00:00\",\"abb\":\"WPG\",\"cPeriod\":\"\",\"gameId\":2015010048,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"away\",\"gs\":1},{\"startTime\":\"2015/09/26 18:00:00\",\"abb\":\"MIN\",\"cPeriod\":\"\",\"gameId\":2015010053,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1},{\"startTime\":\"2015/09/29 21:00:00\",\"abb\":\"ARI\",\"cPeriod\":\"\",\"gameId\":2015010072,\"hg\":{\"caLogo\":\"\",\"verizon\":false,\"usLogo\":\"\"},\"loc\":\"home\",\"gs\":1}]}\n";
}
