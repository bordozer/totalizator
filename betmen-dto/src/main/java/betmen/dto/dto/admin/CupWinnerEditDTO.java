package betmen.dto.dto.admin;

import betmen.dto.dto.TeamDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of = {"cupId", "cupPosition", "teamId"})
public class CupWinnerEditDTO {

    private int cupId;

    @Min(value = 1)
    private int cupPosition;

    @Min(value = 1)
    private int teamId;

    @JsonIgnore
    private TeamDTO team; // bad design, I know

    @JsonProperty
    public TeamDTO getTeam() {
        return team;
    }

    @JsonIgnore
    public void setTeam(final TeamDTO team) {
        this.team = team;
    }
}
