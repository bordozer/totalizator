package totalizator.app.services;

import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

public interface MatchBetsService extends GenericService<MatchBet>{

	public List<MatchBet> loadAll( final User user );

	public List<MatchBet> loadAll( final Match match );
}
