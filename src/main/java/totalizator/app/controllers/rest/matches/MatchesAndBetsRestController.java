package totalizator.app.controllers.rest.matches;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.BetDTO;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/matches")
public class MatchesAndBetsRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private DateTimeService dateTimeService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/bets/", produces = APPLICATION_JSON_VALUE )
	public List<MatchBetDTO> matchesAndBets( final MatchesBetSettingsDTO dto, final Principal principal ) {

		final int userId = dto.getUserId();

		final User user = userId > 0 ? userService.load( userId ) : userService.findByLogin( principal.getName() );
		final List<Match> matches = matchService.loadAll( dto );

		final List<MatchBetDTO> matchBetDTOs = dtoService.getMatchBetForMatches( matches, user );

		if ( userId > 0 ) {

			CollectionUtils.filter( matchBetDTOs, new Predicate<MatchBetDTO>() {
				@Override
				public boolean evaluate( final MatchBetDTO matchBetDTO ) {
					final BetDTO bet = matchBetDTO.getBet();

					if ( bet == null ) {
						return false;
					}

					return bet.getUser().getUserId() == userId;
				}
			} );
		}

		return matchBetDTOs;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{matchId}/bets/{score1}/{score2}/", produces = APPLICATION_JSON_VALUE )
	public BetDTO saveBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "score1" ) int score1, final @PathVariable( "score2" ) int score2 ) {

		final User user = userService.findByLogin( principal.getName() );
		final Match match = matchService.load( matchId );

		if ( ! matchBetsService.userCanBetMatch( match, user ) ) {
			throw new IllegalArgumentException( String.format( "Match betting for cup %s is finished", match.getCup() ) );
		}

		final MatchBet existingBet = matchBetsService.load( user, match );

		if ( existingBet != null ) {

			if ( ! existingBet.getUser().equals( user ) ) {
				throw new IllegalArgumentException( String.format( "Attempt to save bet of %s as %s", existingBet.getUser(), user ) );
			}

			existingBet.setBetScore1( score1 );
			existingBet.setBetScore2( score2 );
			matchBetsService.save( existingBet );

			return dtoService.transformMatchBet( existingBet, user );
		}

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( user );
		matchBet.setMatch( match );
		matchBet.setBetScore1( score1 );
		matchBet.setBetScore2( score2 );
		matchBet.setBetTime( dateTimeService.getNow() );

		final MatchBet result = matchBetsService.save( matchBet );

		return dtoService.transformMatchBet( result, user );
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
}
