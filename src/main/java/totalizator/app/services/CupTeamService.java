package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;

public interface CupTeamService {

	void saveCupTeam( final int cupId, final int teamId, final boolean isActive );

	boolean exists( final Cup cup, final Team team );
}
