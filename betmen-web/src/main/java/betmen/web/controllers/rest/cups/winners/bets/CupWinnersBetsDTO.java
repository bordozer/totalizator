package betmen.web.controllers.rest.cups.winners.bets;

import betmen.dto.dto.TeamDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CupWinnersBetsDTO {
    private int winnersCount;
    private List<TeamDTO> winners;
    private List<UserCupBetsDTO> usersCupBets;
}
