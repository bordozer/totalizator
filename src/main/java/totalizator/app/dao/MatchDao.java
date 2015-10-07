package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchDao extends GenericService<Match> {

	String CACHE_ENTRY = "totalizator.app.cache.match";
	String CACHE_QUERY = "totalizator.app.cache.matches";

	List<Match> loadAll( final Cup cup );

	int getMatchCount( final Team team );

	List<Match> loadAll( final int cupId, final int teamId );

	List<Match> loadAll( final Team team1, final Team team2 );

	List<Match> loadAll( final int cupId, final int team1Id, final int team2Id );

	List<Match> loadAll( final Team team );

	int getMatchCount( final Cup cup );

	int getMatchCount( final Cup cup, final Team team );

	int getFinishedMatchCount( final Cup cup, final Team team );

	int getFutureMatchCount( final Cup cup );

	int getFutureMatchCount( final Cup cup, final Team team );

	Match findByImportId( final String remoteGameId );

	List<Match> loadAllBetween( final LocalDateTime timeFrom, final LocalDateTime timeTo );

	List<Match> loadAllBetween( final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo );

	List<Match> getStartedMatchCount( final Cup cup, final LocalDateTime timeFrom );

	Match getNearestFutureMatch( final Cup cup, final LocalDateTime onTime );

	List<Match> loadAllNotFinished( final int cupId );

	List<Match> loadAllFinished( final int cupId );
}
