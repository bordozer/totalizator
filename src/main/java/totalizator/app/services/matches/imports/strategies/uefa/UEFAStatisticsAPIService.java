package totalizator.app.services.matches.imports.strategies.uefa;

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
	public Set<String> loadRemoteGameIds( final Cup cup, final LocalDate date ) throws IOException {

		final RemoteServerRequest request = new RemoteServerRequest( uefaStatisticsServerURLService.remoteGamesIdsURL( cup, date ) );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteContent = remoteContentService.getRemoteContent( request );

		return uefaGameParsingService.extractRemoteGameIds( remoteContent );
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {

		final RemoteServerRequest request = new RemoteServerRequest( uefaStatisticsServerURLService.loadRemoteGameURL( remoteGameId ) );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteGameJSON = remoteContentService.getRemoteContent( request );

		return uefaGameParsingService.parseGame( remoteGameId, remoteGameJSON );
	}
}