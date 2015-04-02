package totalizator.app.controllers.rest.scores;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.beans.UserPoints;
import totalizator.app.services.score.CupScoresService;
import totalizator.app.services.CupService;
import totalizator.app.services.UserService;

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

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/scores/", produces = APPLICATION_JSON_VALUE )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User user = userService.findByLogin( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( new UserDTO( user.getId(), user.getUsername() ) );

		result.setUserPoints( getUsersScores( cup ) );

		return result;
	}

	private List<UserPointsDTO> getUsersScores( final Cup cup ) {

		return Lists.transform( cupScoresService.getUsersScores( cup ), new Function<UserPoints, UserPointsDTO>() {
			@Override
			public UserPointsDTO apply( final UserPoints userPoints ) {
				return new UserPointsDTO( new UserDTO( userPoints.getUser().getId(), userPoints.getUser().getUsername() ), userPoints.getPoints()  );
			}
		} );
	}
}
