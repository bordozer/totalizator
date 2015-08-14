package totalizator.app.controllers.rest.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/matches" )
public class MatchesRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> matches( final MatchesBetSettingsDTO dto, final Principal principal ) {
		return dtoService.transformMatches( matchService.loadAll( dto ), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> cupMatches( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		final Cup cup = cupService.load( cupId );
		return dtoService.transformMatches( matchService.loadAll( cup ), userService.findByLogin( principal.getName() ) );
	}
}
