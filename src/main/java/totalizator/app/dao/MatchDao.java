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

	List<Match> find( final Team team1, final Team team2 );
}
