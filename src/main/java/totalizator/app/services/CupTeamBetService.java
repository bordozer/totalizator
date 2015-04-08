package totalizator.app.services;

import totalizator.app.enums.CupPosition;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

public interface CupTeamBetService extends GenericService<CupTeamBet> {

	List<CupTeamBet> load( final Cup cup );

	List<CupTeamBet> load( final Cup cup, final Team team );

	List<CupTeamBet> load( final Cup cup, final User user );

	CupTeamBet load( final Cup cup, final User user, final CupPosition cupPosition );

	CupTeamBet load( final Cup cup, final User user, final Team team );
}
