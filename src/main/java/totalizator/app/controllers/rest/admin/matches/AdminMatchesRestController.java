package totalizator.app.controllers.rest.admin.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/matches" )
public class AdminMatchesRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> entries( final MatchesBetSettingsDTO dto, final Principal principal ) {
		return dtoService.transformMatches( matchService.loadAll( dto ), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO create( final @RequestBody MatchDTO matchDTO ) {

		final Match match = new Match();

		dtoService.initMatchFromDTO( matchDTO, match );

		final Match saved = matchService.save( match );

		matchDTO.setMatchId( saved.getId() );

		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO edit( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchDTO matchDTO, final Principal principal ) {

		final Match match = matchService.load( matchDTO.getMatchId() );

		dtoService.initMatchFromDTO( matchDTO, match );

		matchService.save( match );

		return dtoService.transformMatch( match, userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchId}" )
	public void delete( final @PathVariable( "matchId" ) int matchId ) {
		if ( matchId == 0 ) {
			return;
		}

		matchService.delete( matchId );
	}
}
