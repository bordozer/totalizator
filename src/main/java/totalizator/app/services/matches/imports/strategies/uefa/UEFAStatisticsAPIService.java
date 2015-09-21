package totalizator.app.services.matches.imports.strategies.uefa;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.matches.imports.StatisticsServerURLService;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;
import totalizator.app.services.remote.RemoteContentService;
import totalizator.app.services.remote.RemoteServerRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@Service
public class UEFAStatisticsAPIService implements StatisticsServerService {

	private static final String X_AUTH_TOKEN = "a1ce133492c54f0a9aa03ac7242eedeb"; // TODO: move to properties?

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private RemoteGameParsingService uefaGameParsingService;

	@Autowired
	private StatisticsServerURLService uefaStatisticsServerURLService;

	@Override
	public Set<RemoteGame> loadGamesFromJSON( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {

		final RemoteServerRequest request = new RemoteServerRequest( uefaStatisticsServerURLService.remoteGamesIdsURL( cup, dateTo ) );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteContent = remoteContentService.getRemoteContent( request );

		return uefaGameParsingService.loadGamesFromJSON( cup, remoteContent );
	}

	@Override
	public void loadGameFromJSON( final Cup cup, final RemoteGame remoteGame ) throws IOException {

		final RemoteServerRequest request = new RemoteServerRequest( uefaStatisticsServerURLService.loadRemoteGameURL( cup, remoteGame.getRemoteGameId() ) );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteGameJSON = remoteContentService.getRemoteContent( request );

		if ( StringUtils.isEmpty( remoteGameJSON ) ) {
			return;
		}

		uefaGameParsingService.loadGameFromJSON( remoteGame, remoteGameJSON );
	}
}