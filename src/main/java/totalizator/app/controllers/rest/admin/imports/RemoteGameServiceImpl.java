package totalizator.app.controllers.rest.admin.imports;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.imports.nba.NBAGame;

@Service
public class RemoteGameServiceImpl implements RemoteGameService {

	@Override
	public RemoteGame parseGame( final String remoteGameJSON ) {

		final NBAGame nbaGame = new Gson().fromJson( remoteGameJSON, NBAGame.class );

		return new RemoteGame();
	}
}
