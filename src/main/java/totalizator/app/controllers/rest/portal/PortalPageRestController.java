package totalizator.app.controllers.rest.portal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
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
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private DateTimeService dateTimeService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public PortalPageDTO getDefaultLogin( final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final PortalPageDTO result = new PortalPageDTO();

		result.setCupsToShow( dtoService.transformCups( cupService.loadPublicCurrent(), currentUser ) );
		result.setCupsTodayToShow( dtoService.transformCups( getCupsHaveMatchesToday(), currentUser ) );

		return result;
	}

	private List<Cup> getCupsHaveMatchesToday() {

		final List<Match> matchesToday = matchService.loadAllOnDate( dateTimeService.getToday() );

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
