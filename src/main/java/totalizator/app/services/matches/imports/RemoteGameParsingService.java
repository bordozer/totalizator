package totalizator.app.services.matches.imports;

import java.io.IOException;
import java.util.List;

public interface RemoteGameParsingService {

	List<String> extractRemoteGameIds( final String remoteGameJSON );

	RemoteGame parseGame( final String remoteGameId, final String remoteGameJSON ) throws IOException;
}
