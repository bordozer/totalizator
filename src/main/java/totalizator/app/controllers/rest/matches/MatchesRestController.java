package totalizator.app.controllers.rest.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.matches.MatchesAndBetsWidgetService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping( "/rest/matches" )
public class MatchesRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchesAndBetsWidgetService matchesAndBetsWidgetService;

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/" )
	public MatchDTO match( final @PathVariable( "matchId" ) int matchId, final Principal principal ) {
		return dtoService.transformMatch( matchService.load( matchId ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<MatchDTO> matches( final MatchesBetSettingsDTO dto, final Principal principal ) {
		return dtoService.transformMatches( matchesAndBetsWidgetService.loadAll( dto ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/" )
	public List<MatchDTO> cupMatches( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		final Cup cup = cupService.load( cupId );
		return dtoService.transformMatches( matchService.loadAll( cup ), getCurrentUser( principal ) );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
