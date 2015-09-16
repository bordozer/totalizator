package admin.remoteGamesImport.url;

import org.junit.Test;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.strategies.nba.NBAStatisticsServerURLService;
import totalizator.app.services.matches.imports.strategies.uefa.UEFAStatisticsServerURLService;
import totalizator.app.services.utils.DateTimeServiceImpl;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

public class StatisticsServerURLServiceTest {

	private static final String WRONG_URL = "Wrong URL";

	private final TestData testData = new TestData();

	@Test
	public void nbaGameIds() {
		final NBAStatisticsServerURLService urlService = new NBAStatisticsServerURLService();
		assertEquals( WRONG_URL, "http://stats.nba.com/stats/scoreboard?LeagueID=00&gameDate=11/25/2015&DayOffset=0", urlService.remoteGamesIdsURL( testData.cup, testData.date ) );
	}

	@Test
	public void nbaLoadRemoteGameURL() {
		final NBAStatisticsServerURLService urlService = new NBAStatisticsServerURLService();
		assertEquals( WRONG_URL, "http://stats.nba.com/stats/boxscore?GameID=00X12W34&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0", urlService.loadRemoteGameURL( testData.remoteGameId ) );
	}

	@Test
	public void uefaPastGameIds() {

		final DateTimeServiceImpl dateTimeService = getDateTimeService();

		testData.date = dateTimeService.minusDays( 3 ).toLocalDate();

		final UEFAStatisticsServerURLService urlService = new UEFAStatisticsServerURLService();
		urlService.setDateTimeService( dateTimeService );

		assertEquals( WRONG_URL, "http://api.football-data.org/alpha/soccerseasons/77777777/fixtures?timeFrame=p3", urlService.remoteGamesIdsURL( testData.cup, testData.date ) );
	}

	@Test
	public void uefaTodayGameIds() {

		final DateTimeServiceImpl dateTimeService = getDateTimeService();

		testData.date = dateTimeService.getNow().toLocalDate();

		final UEFAStatisticsServerURLService urlService = new UEFAStatisticsServerURLService();
		urlService.setDateTimeService( getDateTimeService() );

		assertEquals( WRONG_URL, "http://api.football-data.org/alpha/soccerseasons/77777777/fixtures?timeFrame=n1", urlService.remoteGamesIdsURL( testData.cup, testData.date ) );
	}

	@Test
	public void uefaFutureGameIds() {

		final DateTimeServiceImpl dateTimeService = getDateTimeService();

		testData.date = dateTimeService.plusDays( 4 ).toLocalDate();

		final UEFAStatisticsServerURLService urlService = new UEFAStatisticsServerURLService();
		urlService.setDateTimeService( getDateTimeService() );

		assertEquals( WRONG_URL, "http://api.football-data.org/alpha/soccerseasons/77777777/fixtures?timeFrame=n5", urlService.remoteGamesIdsURL( testData.cup, testData.date ) );
	}

	@Test
	public void uefaLoadRemoteGameURL() {
		final UEFAStatisticsServerURLService urlService = new UEFAStatisticsServerURLService();
		assertEquals( WRONG_URL, "http://api.football-data.org/alpha/fixtures/00X12W34", urlService.loadRemoteGameURL( testData.remoteGameId ) );
	}

	private DateTimeServiceImpl getDateTimeService() {
		return new DateTimeServiceImpl();
	}

	private class TestData {

		private Cup cup;
		private LocalDate date;
		private String remoteGameId;

		public TestData() {

			cup = new Cup();
			cup.setId( 555 );
			cup.setCupImportId( "77777777" );

			date = LocalDate.of( 2015, 11, 25 );

			remoteGameId = "00X12W34";
		}
	}
}
