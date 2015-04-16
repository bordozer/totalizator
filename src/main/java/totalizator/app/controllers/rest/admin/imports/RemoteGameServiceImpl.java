package totalizator.app.controllers.rest.admin.imports;

import org.springframework.stereotype.Service;

@Service
public class RemoteGameServiceImpl implements RemoteGameService {

	@Override
	public RemoteGame parseGame( final String remoteGameJSON ) {
		return new RemoteGame();
	}
}
