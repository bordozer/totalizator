package totalizator.app.services.matches.imports.strategies;

import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.remote.RemoteContentNullException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

public interface StatisticsServerService {

	Set<RemoteGame> preloadRemoteGames( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException, RemoteContentNullException;

	void populateRemoteGame(final Cup cup, final RemoteGame remoteGame ) throws IOException, RemoteContentNullException;
}
