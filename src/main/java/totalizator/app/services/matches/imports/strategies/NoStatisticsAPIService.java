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
	public Set<RemoteGame> loadGamesFromJSON( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {
		return newHashSet();
	}

	@Override
	public void loadGameFromJSON( Cup cup, final RemoteGame remoteGame ) throws IOException {
	}
}
