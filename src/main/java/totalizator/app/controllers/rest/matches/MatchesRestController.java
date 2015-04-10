package totalizator.app.controllers.rest.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchService;
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
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> entries( final MatchesBetSettingsDTO dto, final Principal principal ) {
		return dtoService.transformMatches( matchService.loadAll( dto ), userService.findByLogin( principal.getName() ) );
	}
}
