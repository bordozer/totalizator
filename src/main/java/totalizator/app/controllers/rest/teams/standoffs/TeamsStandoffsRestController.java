package totalizator.app.controllers.rest.teams.standoffs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/teams" )
public class TeamsStandoffsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "{team1Id}/vs/{team2Id}/", produces = APPLICATION_JSON_VALUE )
	public TeamsStandoffsDTO all( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id, final Principal principal ) {
		return new TeamsStandoffsDTO( dtoService.transformCups( cupService.loadAllPublic(), userService.findByLogin( principal.getName() ) ) );
	}
}
