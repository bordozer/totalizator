package totalizator.app.controllers.rest.admin.imports;

public interface RemoteGameService {

	RemoteGame parseGame( final String remoteGameJSON );
}
