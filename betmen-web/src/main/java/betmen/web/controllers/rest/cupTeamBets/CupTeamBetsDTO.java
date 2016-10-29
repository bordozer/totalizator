package betmen.web.controllers.rest.cupTeamBets;

import betmen.dto.dto.CupTeamBetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupTeamBetsDTO {
    private List<CupTeamBetDTO> cupTeamBets;
}
