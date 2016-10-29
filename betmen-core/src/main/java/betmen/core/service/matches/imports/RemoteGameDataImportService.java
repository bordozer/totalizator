package betmen.core.service.matches.imports;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.service.remote.RemoteContentNullException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface RemoteGameDataImportService {

    Set<RemoteGame> preloadRemoteGames(final LocalDate dateFrom, final LocalDate dateTo, final Cup cup) throws IOException, RemoteContentNullException;

    void loadRemoteGame(final RemoteGame remoteGame, final Cup cupId) throws IOException, RemoteContentNullException;

    Match findMatchFor(final Cup category, final String team1Name, final String team2Name, final LocalDateTime gameDate);

    Match findByRemoteGameId(final String remoteGameId);

    ImportGameResult importGame(final Cup cup, final RemoteGame remoteGame);
}
