package totalizator.app.services.matches.imports.strategies;

import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

public interface StatisticsServerService {

	Collection<String> loadRemoteGameIdsOnDate( final Cup cup, final LocalDate date ) throws IOException;

	RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException;
}
