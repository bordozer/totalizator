package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

public interface MatchBetsService extends GenericService<MatchBet>{

	public List<MatchBet> loadAll( final User user );

	public List<MatchBet> loadAll( final Match match );

	List<MatchBet> loadAll( final Cup cup, final User user );

	public MatchBet load( final User user, final Match match );

	public MatchBet load( final int userId, final int matchId );

	int betsCount( final Match match );

	boolean isBettingAllowed( final Match match, final User user );
}
