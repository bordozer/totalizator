package totalizator.app.controllers.rest.matches.bets;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dto.BetDTO;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
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
	public List<MatchBetsOnDateDTO> matchesAndBets( final MatchesBetSettingsDTO dto, final Principal principal ) {

		final List<Match> matches = matchService.loadAll( dto );

		final int userId = dto.getUserId();

		final User currentUser = userService.findByLogin( principal.getName() );
		final User showBetsOfUser = userId > 0 ? userService.load( userId ) : currentUser;

		final List<MatchBetDTO> matchBetDTOs = dtoService.getMatchBetForMatches( matches, showBetsOfUser, currentUser );

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

		final List<LocalDate> matchDates = matchBetDTOs
				.stream()
				.map( new Function<MatchBetDTO, LocalDate>() {
					@Override
					public LocalDate apply( final MatchBetDTO matchBetDTO ) {
						return matchBetDTO.getMatch().getBeginningTime().toLocalDate();
					}
				} ).distinct().collect( Collectors.toList() );

		final List<MatchBetsOnDateDTO> result = newArrayList();

		matchDates.stream().forEach( new Consumer<LocalDate>() {

			@Override
			public void accept( final LocalDate date ) {

				final List<MatchBetDTO> matchIds = matchBetDTOs
						.stream()
						.filter( new java.util.function.Predicate<MatchBetDTO>() {
							@Override
							public boolean test( final MatchBetDTO matchBetDTO ) {
								return dateTimeService.hasTheSameDate( matchBetDTO.getMatch().getBeginningTime(), date );
							}
						} )
						.collect( Collectors.toList() );

				result.add( new MatchBetsOnDateDTO( date, matchIds ) );
			}
		} );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bet-of-user/{userId}/", produces = APPLICATION_JSON_VALUE )
	public MatchBetDTO matchBet( final @PathVariable( "matchId" ) int matchId, final @PathVariable( "userId" ) int userId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final User showBetOfUser = userId > 0 ? userService.load( userId ) : currentUser;
		final Match match = matchService.load( matchId );

		return dtoService.getMatchBetForMatch( match, showBetOfUser, currentUser );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bets/count/", produces = APPLICATION_JSON_VALUE )
	public int matchBetsCount( final @PathVariable( "matchId" ) int matchId ) {
		return matchBetsService.betsCount( matchService.load( matchId ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{matchId}/bets/{score1}/{score2}/", produces = APPLICATION_JSON_VALUE )
	public BetDTO saveBet( final Principal principal, final @PathVariable( "matchId" ) int matchId, final @PathVariable( "score1" ) int score1, final @PathVariable( "score2" ) int score2 ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Match match = matchService.load( matchId );

		final ValidationResult validationResult = matchBetsService.validateBettingAllowed( match, currentUser );
		if ( ! validationResult.isPassed() ) {
			throw new IllegalArgumentException( validationResult.getMessage() ); // TODO: show the exception to user
		}

		final MatchBet existingBet = matchBetsService.load( currentUser, match );

		if ( existingBet != null ) {

			if ( ! existingBet.getUser().equals( currentUser ) ) {
				throw new IllegalArgumentException( String.format( "Attempt to save bet of %s as %s", existingBet.getUser(), currentUser ) ); // TODO: show the exception to user
			}

			existingBet.setBetScore1( score1 );
			existingBet.setBetScore2( score2 );
			matchBetsService.save( existingBet );

			return dtoService.transformMatchBet( existingBet, currentUser, currentUser );
		}

		final MatchBet matchBet = new MatchBet();
		matchBet.setUser( currentUser );
		matchBet.setMatch( match );
		matchBet.setBetScore1( score1 );
		matchBet.setBetScore2( score2 );
		matchBet.setBetTime( dateTimeService.getNow() );

		final MatchBet result = matchBetsService.save( matchBet );

		return dtoService.transformMatchBet( result, currentUser, currentUser );
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
