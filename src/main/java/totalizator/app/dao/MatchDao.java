package totalizator.app.dao;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.util.List;

public interface MatchDao extends GenericService<Match> {

	String CACHE_ENTRY = "totalizator.app.cache.match";
	String CACHE_QUERY = "totalizator.app.cache.matches";

	@Cacheable( value = CACHE_QUERY )
	List<Match> loadAll( Cup cup );

	@Cacheable( value = CACHE_QUERY )
	List<Match> find( Team team1, Team team2 );
}
