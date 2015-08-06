package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupTeam;
import totalizator.app.models.Team;

import java.util.List;

public interface CupTeamService {

	List<CupTeam> loadAll( final int cupId );

	void saveCupTeam( final int cupId, final int teamId, final boolean isActive );

	boolean exists( final int cupId, final int teamId );

	boolean exists( final Cup cup, final Team team );

	List<Team> loadActiveForCup( final int cupId );

	void clearFor( final int teamId );

	void clearFor( int cupId, int teamId );
}
