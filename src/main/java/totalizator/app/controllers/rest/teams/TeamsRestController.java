package totalizator.app.controllers.rest.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.TeamDTO;
import totalizator.app.services.CupService;
import totalizator.app.services.CupTeamService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;

import java.util.List;

@RestController
@RequestMapping( "/rest/teams" )
public class TeamsRestController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private CupTeamService cupTeamService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<TeamDTO> all() {
		return dtoService.transformTeams( teamService.loadAll() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{teamId}/" )
	public TeamDTO team( final @PathVariable( "teamId" ) int teamId ) {
		return dtoService.transformTeam( teamService.load( teamId ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/" )
	public List<TeamDTO> cupTeams( final @PathVariable( "cupId" ) int cupId ) {
		return dtoService.transformTeams( teamService.loadAll( cupService.load( cupId ).getCategory() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/active/" )
	public List<TeamDTO> cupTeamsActive( final @PathVariable( "cupId" ) int cupId ) {
		return dtoService.transformTeams( cupTeamService.loadActiveForCup( cupId ) );
	}
}
