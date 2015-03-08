package totalizator.app.controllers.rest.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.BetDTO;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/matches")
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

		final User user = userService.findByLogin( principal.getName() );

		final List<Match> matches = matchService.loadOpen();

		final List<MatchBetDTO> result = newArrayList();
		for ( final Match match : matches ) {

			final MatchDTO matchDTO = matchService.initDTOFromModel( match );

			final MatchBet matchBet = matchBetsService.load( user, match );

			if ( matchBet == null ) {
				result.add( new MatchBetDTO( matchDTO ) );
				continue;
			}

			final BetDTO betDTO = new BetDTO( matchDTO, new UserDTO( user.getId(), user.getUsername() ) );
			betDTO.setMatchBetId( matchBet.getId() );
			betDTO.setScore1( matchBet.getBetScore1() );
			betDTO.setScore2( matchBet.getBetScore2() );

			result.add( new MatchBetDTO( matchDTO, betDTO ) );
		}

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{matchId}/bet/{score1}/{score2}/", produces = APPLICATION_JSON_VALUE )
	public void saveBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "score1" ) int score1, final @PathVariable( "score2" ) int score2 ) {

		final User user = userService.findByLogin( principal.getName() );

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( user );
		matchBet.setMatch( matchService.load( matchId ) );
		matchBet.setBetScore1( score1 );
		matchBet.setBetScore2( score2 );
		matchBet.setBetTime( new Date() );

		matchBetsService.save( matchBet );
	}

	/*@ResponseStatus( HttpStatus.OK )
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
	}*/
}