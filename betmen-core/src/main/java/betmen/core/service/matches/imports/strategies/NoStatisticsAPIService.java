package betmen.core.service.matches.imports.strategies;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.RemoteGame;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service
public class NoStatisticsAPIService implements StatisticsServerService {

    @Override
    public Set<RemoteGame> preloadRemoteGames(final Cup cup, final LocalDate dateFrom, final LocalDate dateTo) throws IOException {
        return newHashSet();
    }

    @Override
    public void populateRemoteGame(Cup cup, final RemoteGame remoteGame) throws IOException {
    }
}
