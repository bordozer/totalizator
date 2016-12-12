package betmen.dto.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = {"cup", "team", "user"})
@ToString
public class CupTeamBetDTO {
    private CupDTO cup;
    private TeamDTO team;
    private UserDTO user;
    private int cupPosition;
    private int points;
    private boolean stillActive;
}
