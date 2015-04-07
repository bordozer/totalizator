package totalizator.app.services;

import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Team;

import java.util.List;

public interface DTOService {

	TeamDTO transform( Team team );

	List<TeamDTO> transform( List<Team> teams );
}
