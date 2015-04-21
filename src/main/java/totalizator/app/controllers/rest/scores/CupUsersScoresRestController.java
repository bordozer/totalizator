package totalizator.app.controllers.rest.scores;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.UserPoints;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.UserTitleService;
import totalizator.app.services.score.CupScoresService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/cups/{cupId}")
public class CupUsersScoresRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private CupScoresService cupScoresService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private UserTitleService userTitleService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/scores/", produces = APPLICATION_JSON_VALUE )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( dtoService.transformUser( currentUser ) );

		result.setUserPoints( getUsersScores( cup ) );

		return result;
	}

	private List<UserPointsDTO> getUsersScores( final Cup cup ) {

		return Lists.transform( cupScoresService.getUsersScoresSummary( cup ), new Function<UserPoints, UserPointsDTO>() {
			@Override
			public UserPointsDTO apply( final UserPoints userPoints ) {
				return new UserPointsDTO( dtoService.transformUser( userPoints.getUser() ), userPoints.getPoints(), userTitleService.getUserTitle( userPoints.getUser(), cup ) );
			}
		} );
	}
}
