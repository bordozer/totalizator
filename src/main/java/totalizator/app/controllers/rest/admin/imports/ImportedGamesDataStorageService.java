package totalizator.app.controllers.rest.admin.imports;

import java.io.IOException;

public interface ImportedGamesDataStorageService {

	String getGameData( final int remoteGameId ) throws IOException;

	void store( final int remoteGameId, final String gameJSON ) throws IOException;
}
