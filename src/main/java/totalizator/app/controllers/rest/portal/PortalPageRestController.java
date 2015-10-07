package totalizator.app.controllers.rest.portal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.*;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/portal-page" )
public class PortalPageRestController {

	private static final Logger LOGGER = Logger.getLogger( PortalPageRestController.class );

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private FavoriteCategoryService favoriteCategoryService;

	@Autowired
	private DateTimeService dateTimeService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public PortalPageDTO getDefaultLogin( final PortalPageDTO dto, final Principal principal ) {

		final LocalDate date = dateTimeService.parseDate( dto.getPortalPageDate() );

		final User currentUser = userService.findByLogin( principal.getName() );

		final Comparator<Category> categoryComparator = categoryService.categoriesByFavoritesByName( favoriteCategoryService.loadUserFavoriteCategories( currentUser ) );

		final PortalPageDTO result = new PortalPageDTO();

		result.setCupsToShow( dtoService.transformCups( cupService.loadPublicCurrent()
				.stream()
				.sorted( new Comparator<Cup>() {
					@Override
					public int compare( final Cup o1, final Cup o2 ) {

						final LocalDateTime withoutBetTime1 = matchBetsService.getFirstMatchWithoutBetTime( o1, currentUser );
						final LocalDateTime withoutBetTime2 = matchBetsService.getFirstMatchWithoutBetTime( o2, currentUser );

						if ( withoutBetTime1 != null && withoutBetTime2 != null ) {
							return withoutBetTime1.compareTo( withoutBetTime2 );
						}

						if ( withoutBetTime1 != null ) {
							return -1;
						}

						if ( withoutBetTime2 != null ) {
							return 1;
						}

						return categoryComparator.compare( o1.getCategory(), o2.getCategory() );
					}
				} )
				.collect( Collectors.toList() ), currentUser ) );

		result.setCupsTodayToShow( dtoService.transformCups( getCupsHaveMatchesToday( date )
				.stream()
				.sorted( cupService.categoryNameOrCupNameComparator() )
				.collect( Collectors.toList() ), currentUser ) );

		return result;
	}

	private List<Cup> getCupsHaveMatchesToday( final LocalDate date ) {

		final List<Match> matchesToday = matchService.loadAllOnDate( date );

		return matchesToday
				.stream()
				.filter( new Predicate<Match>() {
					@Override
					public boolean test( final Match match ) {
						return cupService.isCupPublic( match.getCup() );
					}
				} )
				.map( new Function<Match, Cup>() {
					@Override
					public Cup apply( final Match match ) {
						return match.getCup();
					}
				} )
				.distinct()
				.collect( Collectors.toList() );
	}
}
