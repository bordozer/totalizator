package totalizator.app.controllers.rest.bets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.BetDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/bets/users/{userId}")
public class UserBetsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<BetDTO> userBets( final @PathVariable( "userId" ) int userId, final Principal principal ) {

		final User user = userService.load( userId );

		final List<MatchBet> bets = matchBetsService.loadAll( user );

		final User currentUser = userService.findByLogin( principal.getName() );
		return getMatchBetDTOs( user, currentUser, bets );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/matches/{matchId}/", produces = APPLICATION_JSON_VALUE )
	public List<BetDTO> userMatchBets( final @PathVariable( "userId" ) int userId, final @PathVariable( "matchId" ) int matchId, final Principal principal ) {

		final User user = userService.load( userId );
		final Match match = matchService.load( matchId );

		final List<MatchBet> bets = matchBetsService.loadAll( match );

		final User currentUser = userService.findByLogin( principal.getName() );
		return getMatchBetDTOs( user, currentUser, bets );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/cups/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public List<BetDTO> userCupBets( final @PathVariable( "userId" ) int userId, final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User user = userService.load( userId );
		final Cup cup = cupService.load( cupId );

		final List<MatchBet> bets = matchBetsService.loadAll( cup, user );

		final User currentUser = userService.findByLogin( principal.getName() );
		return getMatchBetDTOs( user, currentUser, bets );
	}

	private List<BetDTO> getMatchBetDTOs( final User user, final User accessor, final List<MatchBet> bets ) {
		return dtoService.transformMatchBets( bets, user, accessor );
	}
}
