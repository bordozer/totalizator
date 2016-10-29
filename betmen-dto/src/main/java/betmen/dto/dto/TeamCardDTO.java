package betmen.dto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamCardDTO {
    private TeamDTO team;
    private List<TeamCardCupData> cardCupData;
}
