package betmen.web.controllers.rest.teams.standoffs;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamsStandoffsDTO {
    private TeamDTO team1;
    private TeamDTO team2;
    private CupDTO cupToShow;
    private List<TeamsCupStandoffDTO> standoffsByCup;
    private TeamsLastGamesStat teamsLastGamesStat;
}
