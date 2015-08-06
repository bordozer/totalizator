package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.util.List;

public interface MatchDao extends GenericService<Match> {

	String CACHE_ENTRY = "totalizator.app.cache.match";
	String CACHE_QUERY = "totalizator.app.cache.matches";

	List<Match> loadAll( final Cup cup );

	int getMatchCount( Team team );

	List<Match> loadAll( final Cup cup, final Team team );

	List<Match> loadAll( final Team team1, final Team team2 );

	List<Match> loadAll( final Team team );

	int getMatchCount( final Cup cup );

	int getMatchCount( final Cup cup, final Team team );

	int getFinishedMatchCount( final Cup cup, final Team team );

	int getFutureMatchCount( final Cup cup, final Team team );
}
