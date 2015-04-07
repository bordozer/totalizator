package totalizator.app.controllers.rest.user.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/users/{userId}")
public class UserCardRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/card/", produces = APPLICATION_JSON_VALUE )
	public UserCardDTO cupUsersScores( final @PathVariable( "userId" ) int userId, final Principal principal ) {

		final User user = userService.load( userId );

		final List<MatchBet> matchBets = matchBetsService.loadAll( user );

		final Set<Cup> cupsWhereUserHasBets = newHashSet();
		for ( final MatchBet matchBet : matchBets ) {
			cupsWhereUserHasBets.add( matchBet.getMatch().getCup() );
		}

		final List<Cup> cups = newArrayList( cupsWhereUserHasBets );
		final List<CupDTO> cupDTOs = dtoService.transformCups( cups );

		final UserCardDTO result = new UserCardDTO();
		result.setCupsToShow( cupDTOs );

		return result;
	}
}
