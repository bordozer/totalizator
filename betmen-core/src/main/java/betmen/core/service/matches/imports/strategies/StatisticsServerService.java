package betmen.core.service.matches.imports.strategies;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.remote.RemoteContentNullException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

public interface StatisticsServerService {

    Set<RemoteGame> preloadRemoteGames(final Cup cup, final LocalDate dateFrom, final LocalDate dateTo) throws IOException, RemoteContentNullException;

    void populateRemoteGame(final Cup cup, final RemoteGame remoteGame) throws IOException, RemoteContentNullException;
}
