package totalizator.app.dao;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import java.util.List;

public interface MatchBetDao extends GenericService<MatchBet> {

	String CACHE_ENTRY = "totalizator.app.cache.match-bet";
	String CACHE_QUERY = "totalizator.app.cache.match-bet.query";

	@Cacheable( value = CACHE_QUERY )
	List<MatchBet> loadAll( User user );

	@Cacheable( value = CACHE_QUERY )
	List<MatchBet> loadAll( Match match );

	@Cacheable( value = CACHE_QUERY )
	MatchBet load( User user, Match match );

	@Cacheable( value = CACHE_QUERY )
	int betsCount( Match match );
}
