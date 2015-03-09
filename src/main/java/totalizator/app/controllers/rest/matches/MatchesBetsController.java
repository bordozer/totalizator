package totalizator.app.controllers.rest.matches;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
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
	public List<MatchBetDTO> matchesAndBets( final Principal principal, final @RequestBody MatchesBetSettingsDTO dto ) {

		final List<Match> matches = matchService.loadAll();

		if ( dto.getCategoryId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getCup().getCategory().getId() == dto.getCategoryId();
				}
			} );
		}

		if ( dto.getCupId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getCup().getId() == dto.getCupId();
				}
			} );
		}

		if ( dto.getMatchId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getId() == dto.getMatchId();
				}
			} );
		}

		if ( dto.getMatchId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getId() == dto.getMatchId();
				}
			} );
		}

		return getMatchBetDTOs( userService.findByLogin( principal.getName() ), matches );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/open/", produces = APPLICATION_JSON_VALUE )
	public List<MatchBetDTO> openMatches( final Principal principal ) {
		return getMatchBetDTOs( userService.findByLogin( principal.getName() ), matchService.loadOpen() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{matchId}/bets/{score1}/{score2}/", produces = APPLICATION_JSON_VALUE )
	public void saveBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "score1" ) int score1, final @PathVariable( "score2" ) int score2 ) {

		final User user = userService.findByLogin( principal.getName() );

		final Match match = matchService.load( matchId );
		final MatchBet savedMatchBet = matchBetsService.load( user, match );
		if ( savedMatchBet != null ) {
			savedMatchBet.setBetScore1( score1 );
			savedMatchBet.setBetScore2( score2 );
			matchBetsService.save( savedMatchBet );

			return;
		}

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( user );
		matchBet.setMatch( match );
		matchBet.setBetScore1( score1 );
		matchBet.setBetScore2( score2 );
		matchBet.setBetTime( new Date() );

		matchBetsService.save( matchBet );
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

	private List<MatchBetDTO> getMatchBetDTOs( final User user, final List<Match> matches ) {
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
}
