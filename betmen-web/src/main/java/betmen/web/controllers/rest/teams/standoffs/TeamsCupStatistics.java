package betmen.web.controllers.rest.teams.standoffs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamsCupStatistics {
    private int team1won;
    private int team1lost;
    private int team1Total;

    private int team2won;
    private int team2lost;
    private int team2Total;
}
