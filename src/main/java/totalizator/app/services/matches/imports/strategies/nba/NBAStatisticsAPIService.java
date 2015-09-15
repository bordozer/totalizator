package totalizator.app.services.matches.imports.strategies.nba;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.ImportedGamesDataStorageService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.matches.imports.StatisticsServerURLService;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;
import totalizator.app.services.remote.RemoteContentService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@Service
public class NBAStatisticsAPIService implements StatisticsServerService {

	public static final String NBA = "NBA";

	/*
		More about NBS API: https://github.com/ins429/ins
	*/

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private RemoteGameParsingService nbaGameParsingService;

	@Autowired
	private ImportedGamesDataStorageService importedGamesDataStorageService;

	@Autowired
	private StatisticsServerURLService nbaStatisticsServerURLService;

	@Override
	public Set<String> loadRemoteGameIds( final Cup cup, final LocalDate date ) throws IOException {
		return nbaGameParsingService.extractRemoteGameIds( remoteContentService.getRemoteContent( nbaStatisticsServerURLService.remoteGamesIdsURL( cup, date ) ) );
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {

		final String remoteGameJSON = getRemoteGameJSON( remoteGameId );

		if ( StringUtils.isEmpty( remoteGameJSON ) ) {
			return null;
		}

		final RemoteGame remoteGame = nbaGameParsingService.parseGame( remoteGameId, remoteGameJSON );

		if ( remoteGame.isFinished() ) {
			importedGamesDataStorageService.store( NBA, remoteGameId, remoteGameJSON );
		}

		return remoteGame;
	}

	private String getRemoteGameJSON( final String remoteGameId ) throws IOException {

		final String gameJSON = importedGamesDataStorageService.getGameData( NBA, remoteGameId );
		if ( StringUtils.isNotEmpty( gameJSON ) ) {
			return gameJSON;
		}

		return remoteContentService.getRemoteContent( nbaStatisticsServerURLService.loadRemoteGameURL( remoteGameId ) );
	}
}
