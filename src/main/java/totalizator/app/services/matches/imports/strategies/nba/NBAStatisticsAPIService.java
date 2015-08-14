package totalizator.app.services.matches.imports.strategies.nba;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.services.RemoteContentService;
import totalizator.app.services.matches.imports.ImportedGamesDataStorageService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class NBAStatisticsAPIService implements StatisticsServerService {

	public static final String NBA = "NBA";

	/*
		More about NBS API: https://github.com/ins429/ins
	*/

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private RemoteGameParsingService remoteGameParsingService;

	@Autowired
	private ImportedGamesDataStorageService importedGamesDataStorageService;

	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {

		final String remoteGameJSON = getRemoteGameJSON( remoteGameId );

		if ( StringUtils.isEmpty( remoteGameJSON ) ) {
			return null;
		}

		final RemoteGame remoteGame = remoteGameParsingService.parseGame( remoteGameJSON );

		if ( remoteGame.isFinished() ) {
			importedGamesDataStorageService.store( NBA, remoteGameId, remoteGameJSON );
		}

		return remoteGame;
	}

	public List<String> loadRemoteGameIdsOnDate( final LocalDate date ) throws IOException {
		// import by date ( 05/01/2015 mm/dd/yyyy - first of may)
		// http://stats.nba.com/stats/scoreboard?LeagueID=00&gameDate=05/01/2015&DayOffset=0
		final String url = String.format( "http://stats.nba.com/stats/scoreboard?LeagueID=00&gameDate=%s/%s/%s&DayOffset=0", date.getMonthValue(), date.getDayOfMonth(), date.getYear() );
		return remoteGameParsingService.extractRemoteGameIds( remoteContentService.getRemoteContent( url ) );
	}

	private String getRemoteGameJSON( final String remoteGameId ) throws IOException {
		// Game ID format: 0021401217
		// 0 		- ?
		// 02 		- league code
		// 14 		- years of cup
		// 0		- ?
		// 1217		- game ID
		final String url = String.format( "http://stats.nba.com/stats/boxscore?GameID=%s&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0", remoteGameId );

		final String gameJSON = importedGamesDataStorageService.getGameData( NBA, remoteGameId );
		if ( StringUtils.isNotEmpty( gameJSON ) ) {
			return gameJSON;
		}

		return remoteContentService.getRemoteContent( url );
	}
}
