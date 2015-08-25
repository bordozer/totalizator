package totalizator.app.services.teams;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;

import java.util.List;

public interface TeamsStandoffService {

	Cup getLastStandoffCup( Team team1, Team team2 );

	List<TeamsCupStandoff> getTeamsStandoffByCups( Team team1, Team team2 );
}
