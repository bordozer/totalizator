package totalizator.app.services.matches.imports;

import totalizator.app.models.Cup;

import java.io.IOException;
import java.util.Set;

public interface RemoteGameParsingService {

	Set<RemoteGame> loadGamesFromJSON( final Cup cup, final String remoteGamesJSON );

	void loadGameFromJSON( final RemoteGame remoteGame, final String remoteGameJSON ) throws IOException;
}
