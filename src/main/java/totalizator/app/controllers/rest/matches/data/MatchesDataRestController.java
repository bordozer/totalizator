package totalizator.app.controllers.rest.matches.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.services.CategoryService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/matches/filter-data" )
public class MatchesDataRestController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public MatchesDataDTO matchesAndBets( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final MatchesDataDTO result = new MatchesDataDTO();

		result.setCategories( dtoService.transformCategories( categoryService.loadAll() ) );
		result.setTeams( dtoService.transformTeams( teamService.loadAll() ) );
		result.setUsers( dtoService.transformUsers( userService.loadAll() ) );

		return result;
	}
}
