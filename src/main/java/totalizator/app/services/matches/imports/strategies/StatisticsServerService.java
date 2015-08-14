package totalizator.app.services.matches.imports.strategies;

import totalizator.app.services.matches.imports.RemoteGame;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

public interface StatisticsServerService {

	Collection<? extends String> loadRemoteGameIdsOnDate( final LocalDate date ) throws IOException;

	RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException;
}
