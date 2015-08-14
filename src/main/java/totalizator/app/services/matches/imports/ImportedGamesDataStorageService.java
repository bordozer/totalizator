package totalizator.app.services.matches.imports;

import java.io.IOException;

public interface ImportedGamesDataStorageService {

	String getGameData( final String folder, final String remoteGameId ) throws IOException;

	void store( final String folder, final String remoteGameId, final String gameJSON ) throws IOException;
}
