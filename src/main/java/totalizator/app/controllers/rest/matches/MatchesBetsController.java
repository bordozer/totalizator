package totalizator.app.controllers.rest.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.*;
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
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchBetDTO> matchesAndBets( final MatchesBetSettingsDTO dto, final Principal principal ) {
		return getMatchBetDTOs( principal, matchService.loadAll( dto ) );
	}

	/*@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/open/", produces = APPLICATION_JSON_VALUE )
	public List<MatchBetDTO> openMatches( final Principal principal ) {
		return getMatchBetDTOs( principal, matchService.loadOpen() );
	}*/

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{matchId}/bets/{score1}/{score2}/", produces = APPLICATION_JSON_VALUE )
	public BetDTO saveBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "score1" ) int score1, final @PathVariable( "score2" ) int score2 ) {

		final User user = userService.findByLogin( principal.getName() );

		final Match match = matchService.load( matchId );
		final MatchBet existingBet = matchBetsService.load( user, match );
		if ( existingBet != null ) {
			existingBet.setBetScore1( score1 );
			existingBet.setBetScore2( score2 );
			matchBetsService.save( existingBet );

			return getBetDTO( existingBet, user );
		}

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( user );
		matchBet.setMatch( match );
		matchBet.setBetScore1( score1 );
		matchBet.setBetScore2( score2 );
		matchBet.setBetTime( new Date() );

		final MatchBet result = matchBetsService.save( matchBet );

		return getBetDTO( result, user );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchId}/bets/{matchBetId}" )
	public void deleteBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "matchBetId" ) int matchBetId ) {

		if ( matchBetId == 0 ) {
			return;
		}

		matchBetsService.delete( matchBetId );
	}

	private List<MatchBetDTO> getMatchBetDTOs( final Principal principal, final List<Match> matches ) {
		final User user = userService.findByLogin( principal.getName() );

		final List<MatchBetDTO> result = newArrayList();
		for ( final Match match : matches ) {

			final MatchDTO matchDTO = matchService.initDTOFromModel( match );

			final MatchBetDTO matchBetDTO = new MatchBetDTO( matchDTO );
			matchBetDTO.setBettingAllowed( matchBetsService.isBettingAllowed( match, user ) );

			final MatchBet matchBet = matchBetsService.load( user, match );

			if ( matchBet == null ) {
				result.add( matchBetDTO );
				continue;
			}

			final BetDTO betDTO = getBetDTO( matchBet, user );

			matchBetDTO.setBet( betDTO );

			result.add( matchBetDTO );
		}

		return result;
	}

	private BetDTO getBetDTO( final MatchBet matchBet, final User user ) {
		final MatchDTO matchDTO = matchService.initDTOFromModel( matchBet.getMatch() );
		final BetDTO betDTO = new BetDTO( matchDTO, new UserDTO( user.getId(), user.getUsername() ) );
		betDTO.setMatchBetId( matchBet.getId() );
		betDTO.setScore1( matchBet.getBetScore1() );
		betDTO.setScore2( matchBet.getBetScore2() );

		return betDTO;
	}
}
