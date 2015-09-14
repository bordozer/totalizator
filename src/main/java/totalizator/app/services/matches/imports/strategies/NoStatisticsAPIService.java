package totalizator.app.services.matches.imports.strategies;

import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.RemoteGame;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service
public class NoStatisticsAPIService implements StatisticsServerService {

	@Override
	public Set<String> loadRemoteGameIds( final Cup cup, final LocalDate date ) throws IOException {
		return newHashSet();
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {
		return null;
	}
}
