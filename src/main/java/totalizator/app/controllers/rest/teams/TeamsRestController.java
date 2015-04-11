package totalizator.app.controllers.rest.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.dto.TeamDTO;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/teams" )
public class TeamsRestController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<TeamDTO> all() {
		return dtoService.transformTeams( teamService.loadAll() );
	}
}
