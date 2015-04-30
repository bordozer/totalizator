package totalizator.app.controllers.rest.matches.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/matches/filter-data" )
public class MatchesDataRestController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public MatchesDataDTO matchesAndBets( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final boolean isAdmin = securityService.isAdmin( currentUser );

		final Cup cup = cupService.load( cupId );

		final MatchesDataDTO result = new MatchesDataDTO();

		result.setCategories( dtoService.transformCategories( categoryService.loadAll() ) );
		result.setCups( dtoService.transformCups( isAdmin ? cupService.loadAll() : cupService.loadAllPublic(), currentUser ) );
		result.setTeams( dtoService.transformTeams( teamService.loadAll() ) );
		result.setUsers( dtoService.transformUsers( userService.loadAll() ) );

		return result;
	}
}
