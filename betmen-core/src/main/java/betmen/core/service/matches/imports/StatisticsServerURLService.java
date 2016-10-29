package betmen.core.service.matches.imports;

import betmen.core.entity.Cup;

import java.time.LocalDate;

public interface StatisticsServerURLService {

    String remoteGamesIdsURL(final Cup cup, final LocalDate date);

    String loadRemoteGameURL(final Cup cup, final String remoteGameId);
}
