package totalizator.app.services.matches.imports;

import totalizator.app.models.Cup;

import java.time.LocalDate;

public interface StatisticsServerURLService {

	String remoteGamesIdsURL( final Cup cup, final LocalDate date );

	String loadRemoteGameURL( final String remoteGameId );
}
