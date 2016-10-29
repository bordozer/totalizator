package betmen.web.controllers.rest.teams.standoffs;

import betmen.dto.dto.CupDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamsLastGamesStat {
    private CupDTO cup;
    private int team1CurrentCupLastGames;
    private long team1CurrentCupLastGamesWon;
    private int team2CurrentCupLastGames;
    private long team2CurrentCupLastGamesWon;
}
