package totalizator.app.controllers.rest.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Category;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping( "/rest/teams" )
public class TeamsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupService cupService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private CupTeamService cupTeamService;

	// Makes performance very slow
	/*@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<TeamDTO> all( final Principal principal ) {
		return dtoService.transformTeams( teamService.loadAll(), getCurrentUser( principal ) );
	}*/

	@RequestMapping( method = RequestMethod.GET, value = "/{teamId}/" )
	public TeamDTO team( final @PathVariable( "teamId" ) int teamId, final Principal principal ) {
		return dtoService.transformTeam( teamService.load( teamId ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/categories/{categoryId}/" )
	public List<TeamDTO> categoryTeams( final @PathVariable( "categoryId" ) int categoryId, final Principal principal ) {
		final Category category = categoryService.load( categoryId );
		return dtoService.transformTeams( category, teamService.loadAll( category ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/" )
	public List<TeamDTO> cupTeams( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		final Category category = cupService.load( cupId ).getCategory();
		return dtoService.transformTeams( category, teamService.loadAll( category ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/active/" )
	public List<TeamDTO> cupTeamsActive( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		return dtoService.transformTeams( cupService.load( cupId ).getCategory(), cupTeamService.loadActiveForCup( cupId ), getCurrentUser( principal ) );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
