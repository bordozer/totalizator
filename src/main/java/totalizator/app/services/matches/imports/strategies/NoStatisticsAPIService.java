package totalizator.app.services.matches.imports.strategies;

import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import static com.google.common.collect.Lists.newArrayList;

@Service
public class NoStatisticsAPIService implements StatisticsServerService {
	@Override
	public Collection<String> loadRemoteGameIdsOnDate( final Cup cup, final LocalDate date ) throws IOException {
		return newArrayList();
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {
		return null;
	}
}
