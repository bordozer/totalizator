package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchBetDao extends GenericService<MatchBet> {

	String CACHE_ENTRY = "totalizator.app.cache.match-bet";
	String CACHE_QUERY = "totalizator.app.cache.match-bet.query";

	List<MatchBet> loadAll( final User user );

	List<MatchBet> loadAll( final Match match );

	MatchBet load( final User user, final Match match );

	int betsCount( final Match match );

	int betsCount( final Cup cup, final User user );

	int getMatchesCountAccessibleBorBettingSince( final Cup cup, final User user, final LocalDateTime sinceTime );

	Match getFirstMatchWithoutBetSince( final Cup cup, final User user, final LocalDateTime sinceTime );
}
