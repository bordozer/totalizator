package totalizator.app.dao;

import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import java.util.List;

public interface MatchBetDao extends GenericService<MatchBet> {

	String CACHE_ENTRY = "totalizator.app.cache.match-bet";
	String CACHE_QUERY = "totalizator.app.cache.match-bet.query";

	List<MatchBet> loadAll( User user );

	List<MatchBet> loadAll( Match match );

	MatchBet load( User user, Match match );

	int betsCount( Match match );
}
