package betmen.web.controllers.rest.admin.imports;

import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.TeamDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RemoteGameLocalData {
    private TeamDTO team1;
    private TeamDTO team2;
    private MatchDTO match;
}
