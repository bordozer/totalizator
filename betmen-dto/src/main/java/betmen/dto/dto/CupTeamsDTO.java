package betmen.dto.dto;

import betmen.dto.dto.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CupTeamsDTO {
    private List<TeamDTO> teams;
    private Set<String> letters;
}
