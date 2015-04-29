package totalizator.app.controllers.rest.admin.imports;

import totalizator.app.models.Cup;

import java.io.IOException;

public interface ImportedGamesDataStorageService {

	String getGameData( final Cup cup, final String remoteGameId ) throws IOException;

	void store( final Cup cup, final String remoteGameId, final String gameJSON ) throws IOException;
}
