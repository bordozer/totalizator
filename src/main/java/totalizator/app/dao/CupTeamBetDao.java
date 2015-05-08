package totalizator.app.dao;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import java.util.List;

public interface CupTeamBetDao extends GenericService<CupTeamBet> {

	String CACHE_ENTRY = "totalizator.app.cache.user-cup-winner-bet";
	String CACHE_QUERY = "totalizator.app.cache.user-cup-winner-bet.query";

	@Cacheable( value = CACHE_QUERY )
	List<CupTeamBet> load( Cup cup, User user );

	@Cacheable( value = CACHE_QUERY )
	CupTeamBet load( Cup cup, User user, int cupPosition );

	@Cacheable( value = CACHE_QUERY )
	CupTeamBet load( Cup cup, Team team, User user );

	@Cacheable( value = CACHE_QUERY )
	List<CupTeamBet> load( Cup cup );
}
