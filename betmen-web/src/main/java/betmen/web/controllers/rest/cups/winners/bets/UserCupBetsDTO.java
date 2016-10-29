package betmen.web.controllers.rest.cups.winners.bets;

import betmen.dto.dto.CupTeamBetDTO;
import betmen.dto.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserCupBetsDTO {
    private UserDTO user;
    private List<CupTeamBetDTO> userCupBets;
}
