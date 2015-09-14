package totalizator.app.services.matches.imports;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface RemoteGameDataImportService {

	Set<String> loadRemoteGameIds( final LocalDate dateFrom, final LocalDate dateTo, final Cup cup ) throws IOException;

	RemoteGame loadRemoteGame( final String remoteGameId, final Cup cupId ) throws IOException;

	Match findMatchFor( final Cup category, final String team1Name, final String team2Name, final LocalDateTime gameDate );

	Match findByRemoteGameId( final String remoteGameId );

	void importGame( final Cup cup, final RemoteGame remoteGame );
}
