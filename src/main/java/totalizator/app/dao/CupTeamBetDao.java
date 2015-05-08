package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import java.util.List;

public interface CupTeamBetDao extends GenericService<CupTeamBet> {

	String CACHE_ENTRY = "totalizator.app.cache.user-cup-winner-bet";
	String CACHE_QUERY = "totalizator.app.cache.user-cup-winner-bet.query";

	List<CupTeamBet> load( final Cup cup, final User user );

	CupTeamBet load( final Cup cup, final User user, final int cupPosition );

	CupTeamBet load( final Cup cup, final Team team, final User user );

	List<CupTeamBet> load( final Cup cup );
}