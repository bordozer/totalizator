package totalizator.app.controllers.rest.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.User;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/matches" )
public class MatchesBetsController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/open/", produces = APPLICATION_JSON_VALUE )
	public List<MatchBetDTO> openMatches( final Principal principal ) {

//		final User user = userService.findByLogin( principal.getName() );

		final List<MatchDTO> matchDTOs = Lists.transform( matchService.loadOpen(), matchService::initDTOFromModel );
		return Lists.transform( matchDTOs, MatchBetDTO::new );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/bets/users/{userId}/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> userBets( final @PathVariable( "userId" ) int userId ) {
		return Lists.transform( matchService.loadAll(), matchService::initDTOFromModel );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/bets/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO betMatch( final @RequestBody MatchDTO matchDTO ) {
		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchBetId}/bets/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO editBet( final @PathVariable( "matchBetId" ) int matchBetId, final @RequestBody MatchDTO matchDTO ) {
		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchBetId}" )
	public void deleteBet( final @PathVariable( "matchBetId" ) int matchBetId ) {

		if ( matchBetId == 0 ) {
			return;
		}

		matchBetsService.delete( matchBetId );
	}
}
