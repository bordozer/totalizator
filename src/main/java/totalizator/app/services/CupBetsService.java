package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

public interface CupBetsService extends GenericService<CupTeamBet> {

	List<CupTeamBet> load( final Cup cup, final User user );

	CupTeamBet load( final Cup cup, final User user, final int cupPosition );

	CupTeamBet load( final Cup cup, final User user, final Team team );
}
