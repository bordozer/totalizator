package totalizator.app.services.matches.imports;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.services.remote.RemoteContentNullException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface RemoteGameDataImportService {

	Set<RemoteGame> preloadRemoteGames( final LocalDate dateFrom, final LocalDate dateTo, final Cup cup ) throws IOException, RemoteContentNullException;

	void loadRemoteGame( final RemoteGame remoteGame, final Cup cupId ) throws IOException, RemoteContentNullException;

	Match findMatchFor( final Cup category, final String team1Name, final String team2Name, final LocalDateTime gameDate );

	Match findByRemoteGameId( final String remoteGameId );

	ImportGameResult importGame( final Cup cup, final RemoteGame remoteGame );
}
