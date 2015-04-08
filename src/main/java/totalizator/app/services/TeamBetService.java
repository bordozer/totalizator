package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.TeamBet;
import totalizator.app.models.User;

import java.util.List;

public interface TeamBetService extends GenericService<TeamBet> {

	List<TeamBet> load( final Cup cup );

	List<TeamBet> load( final User user );

	List<TeamBet> load( final Cup cup, final User user );
}
