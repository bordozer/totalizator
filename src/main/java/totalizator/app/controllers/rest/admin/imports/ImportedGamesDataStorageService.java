package totalizator.app.controllers.rest.admin.imports;

import java.io.IOException;

public interface ImportedGamesDataStorageService {

	String getGameData( final String remoteGameId ) throws IOException;

	void store( final String remoteGameId, final String gameJSON ) throws IOException;
}
