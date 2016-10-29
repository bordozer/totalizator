package betmen.web.controllers.rest.teams.standoffs;

import betmen.dto.dto.CupDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamsCupStandoffDTO {
    private CupDTO cup;
    private int score1;
    private int score2;
}
