package admin.remoteGamesImport;

import org.easymock.EasyMock;
import org.junit.Before;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.GameImportStrategyType;
import totalizator.app.services.matches.imports.RemoteGameDataImportServiceImpl;
import totalizator.app.services.matches.imports.strategies.nba.NBAStatisticsAPIService;
import totalizator.app.services.matches.imports.strategies.uefa.UEFAStatisticsAPIService;
import totalizator.app.services.utils.DateTimeServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newTreeSet;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RemoteGameDataImportServiceTest {

	private static final List<String> EXPECTED_REMOTE_GAME_IDS = newArrayList( "0011500004", "0011500005", "0011500006" );

	@Before
	public void setup() {

	}

//	@Test
	public void loadRemoteGameIds() throws IOException {

		final TestData testData = new TestData();

		final DateTimeServiceImpl dateTimeService = getDateTimeService();

		testData.dateFrom = dateTimeService.plusDays( 4 ).toLocalDate();
		testData.dateTo = dateTimeService.plusDays( 4 ).toLocalDate();
		testData.gameImportStrategyType = GameImportStrategyType.NBA;

		final RemoteGameDataImportServiceImpl remoteGameDataImportService = getRemoteGameDataImportService( testData );

		final Set<String> remoteGameIds = remoteGameDataImportService.loadRemoteGameIds( testData.dateFrom, testData.dateTo, testData.cup );

		assertEquals( remoteGameIds.size(), EXPECTED_REMOTE_GAME_IDS.size() );

		for ( final String remoteGameId : EXPECTED_REMOTE_GAME_IDS ) {
			assertTrue( remoteGameIds.contains( remoteGameId ) );
		}

		int i = 0;
		for ( final String remoteGameId : remoteGameIds ) {
			assertEquals( EXPECTED_REMOTE_GAME_IDS.get( i ), remoteGameId );

			i++;
		}
	}

	private RemoteGameDataImportServiceImpl getRemoteGameDataImportService( final TestData testData ) throws IOException {

		final RemoteGameDataImportServiceImpl remoteGameDataImportService = new RemoteGameDataImportServiceImpl();

		remoteGameDataImportService.setDateTimeService( getDateTimeService() );

		remoteGameDataImportService.setNbaStatisticsAPIService( getNbaStatisticsAPIService( testData ) );
		remoteGameDataImportService.setUefaStatisticsAPIService( getUefaStatisticsAPIService( testData ) );

		return remoteGameDataImportService;
	}

	private NBAStatisticsAPIService getNbaStatisticsAPIService( final TestData testData ) throws IOException {

		final NBAStatisticsAPIService nbaStatisticsAPIService = EasyMock.createMock( NBAStatisticsAPIService.class );

		final Set<String> day4GameIds = newTreeSet();
		day4GameIds.add( "0011500004" );
		day4GameIds.add( "0011500005" );
		day4GameIds.add( "0011500006" );

		EasyMock.expect( nbaStatisticsAPIService.loadRemoteGameIds( testData.cup, testData.dateFrom ) ).andReturn( day4GameIds ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( nbaStatisticsAPIService );

		return nbaStatisticsAPIService;
	}

	private UEFAStatisticsAPIService getUefaStatisticsAPIService( final TestData testData ) {
		return null;
	}

	private DateTimeServiceImpl getDateTimeService() {
		return new DateTimeServiceImpl();
	}

	private class TestData {

		private Cup cup;

		private LocalDate dateFrom;
		private LocalDate dateTo;

		private GameImportStrategyType gameImportStrategyType;

		public TestData() {

			cup = new Cup();
			cup.setId( 555 );
			cup.setCupImportId( "77777777" );
		}
	}
}
