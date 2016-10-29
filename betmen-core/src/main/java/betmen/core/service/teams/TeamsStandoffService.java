package betmen.core.service.teams;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;

import java.util.List;

public interface TeamsStandoffService {

    Cup getLastStandoffCup(Team team1, Team team2);

    List<TeamsCupStandoff> getTeamsStandoffByCups(Team team1, Team team2);
}
