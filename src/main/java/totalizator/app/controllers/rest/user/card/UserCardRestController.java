package totalizator.app.controllers.rest.user.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.CupsAndMatchesService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/users/{userId}" )
public class UserCardRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private CupsAndMatchesService cupsAndMatchesService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/card/", produces = APPLICATION_JSON_VALUE )
	public UserCardDTO userCard(
			final @PathVariable( "userId" ) int userId
			, final @RequestParam( value = "cupId", required = false ) Integer cupId
			, final UserCardParametersDTO parameters
			, final Principal principal
	) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final LocalDate date = dateTimeService.parseDate( parameters.getOnDate() );

		final List<Cup> cupsByParameters = getCupsToShow( userId, cupId, date );

		final List<CupDTO> cupDTOs = dtoService.transformCups( cupsByParameters
				.stream()
				.sorted( cupService.categoryNameOrCupNameComparator() )
				.collect( Collectors.toList() ), currentUser );

		return new UserCardDTO( cupDTOs );
	}

	private List<Cup> getCupsToShow( final int userId, final Integer cupId, final LocalDate date ) {

		final List<Cup> cupsByParameters = getCupsToShow( cupId, date );
		final List<Cup> cupsWhereUserHasBets = getCupsWhereUserHasBets( userId, date );

		return cupsByParameters
				.stream()
				.filter( new Predicate<Cup>() {
					@Override
					public boolean test( final Cup cup ) {
						return cupsWhereUserHasBets.contains( cup );
					}
				} )
				.collect( Collectors.toList() );
	}

	private List<Cup> getCupsWhereUserHasBets( final @PathVariable( "userId" ) int userId, final LocalDate date ) {

		final User userWBetsIsShowing = userService.load( userId );
		final List<MatchBet> matchBets = matchBetsService.loadAll( userWBetsIsShowing, date );

		return matchBets
				.stream()
				.map( new Function<MatchBet, Cup>() {
					@Override
					public Cup apply( final MatchBet matchBet ) {
						return matchBet.getMatch().getCup();
					}
				} )
				.distinct()
				.collect( Collectors.toList() );
	}

	private List<Cup> getCupsToShow( final Integer cupId, final LocalDate date ) {

		if ( cupId != null && cupId > 0 ) {
			return newArrayList( cupService.load( cupId ) );
		}

		return cupsAndMatchesService.getCupsHaveMatchesOnDate( date );
	}
}
