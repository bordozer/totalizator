package totalizator.app.controllers.rest.matches.bets.collapsed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@RestController
@RequestMapping( "/rest/matches/bets/collapsed" )
public class MatchesAndBetsCollapsedRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public MatchesAndBetsCollapsedDTO matchesAndBetsCollapsed( final MatchesBetSettingsDTO filter, final Principal principal ) {

		final int userId = filter.getUserId();

		final User currentUser = userService.findByLogin( principal.getName() );
		final User showBetsOfUser = userId > 0 ? userService.load( userId ) : currentUser;

		final Cup cup = cupService.load( filter.getCupId() );

		final MatchesAndBetsCollapsedDTO result = new MatchesAndBetsCollapsedDTO( dtoService.transformCup( cup, showBetsOfUser ), dtoService.transformUser( showBetsOfUser ) );

		final List<Match> matches = matchService.loadAll( filter );
		result.setMatchesCount( matches.size() );

		result.setNowPlayingMatchesCount( getMatchesNowCount( matches ) );
		result.setTodayMatchesCount( getTodayMatchesCount( matches ) );

		final List<MatchBetDTO> matchBetDTOs = dtoService.getMatchBetForMatches( matches, showBetsOfUser, currentUser );
		final int userBetsCount = ( int ) matchBetDTOs
				.stream().filter( new Predicate<MatchBetDTO>() {
					@Override
					public boolean test( final MatchBetDTO match ) {
						return match.getBet() != null;
					}
				} ).count();
		result.setUserBetsCount( userBetsCount );

		final int matchesWithoutBetsCount = ( int ) matchBetDTOs
				.stream().filter( new Predicate<MatchBetDTO>() {
					@Override
					public boolean test( final MatchBetDTO match ) {
						return match.getBet() == null && match.getMatch().getBeginningTime().isAfter( dateTimeService.getNow() );
					}
				} ).count();
		result.setMatchesWithoutBetsCount( matchesWithoutBetsCount );

		if ( matches.size() > 0 ) {
			result.setFirstMatchTime( matches.get( 0 ).getBeginningTime() );
		}

		if ( matchBetDTOs.size() > 0 ) {

			final Predicate<MatchBetDTO> noBetPredicate = new Predicate<MatchBetDTO>() {
				@Override
				public boolean test( final MatchBetDTO matchBetDTO ) {
					return matchBetDTO.getBet() == null && matchBetDTO.getMatch().getBeginningTime().isAfter( dateTimeService.getNow() );
				}
			};

			final Function<MatchBetDTO, LocalDateTime> function = new Function<MatchBetDTO, LocalDateTime>() {
				@Override
				public LocalDateTime apply( final MatchBetDTO matchBetDTO ) {
					return matchBetDTO.getMatch().getBeginningTime();
				}
			};

			final Optional<LocalDateTime> localDateTime = matchBetDTOs
					.stream()
					.filter( noBetPredicate )
					.findFirst()
					.map( function );

			if ( localDateTime.isPresent() ) {
				result.setFirstMatchNoBetTime( localDateTime.get() );
			}

		}

		return result;
	}

	private int getTodayMatchesCount( final List<Match> matches ) {

		return ( int ) matches
				.stream()
				.filter( new Predicate<Match>() {
					@Override
					public boolean test( final Match match ) {
						return dateTimeService.hasTheSameDate( match.getBeginningTime(), dateTimeService.getNow() );
					}
				} )
				.count();
	}

	private int getMatchesNowCount( final List<Match> matches ) {

		return ( int ) matches
				.stream()
				.filter( new Predicate<Match>() {
					@Override
					public boolean test( final Match match ) {
						return matchService.isMatchStarted( match );
					}
				} )
				.count();
	}
}
