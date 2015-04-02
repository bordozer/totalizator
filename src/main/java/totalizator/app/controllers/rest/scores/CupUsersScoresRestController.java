package totalizator.app.controllers.rest.scores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/cups/{cupId}")
public class CupUsersScoresRestController {

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/scores/", produces = APPLICATION_JSON_VALUE )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User user = userService.findByLogin( principal.getName() );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( new UserDTO( user.getId(), user.getUsername() ) );

		result.setUserPoints( getUserPoints() );

		return result;
	}

	private List<UserPointsDTO> getUserPoints() {

		final List<UserPointsDTO> result = newArrayList();

		result.add( new UserPointsDTO( new UserDTO( 1, "User 1" ), 15 ) );
		result.add( new UserPointsDTO( new UserDTO( 2, "User 2" ), 13 ) );
		result.add( new UserPointsDTO( new UserDTO( 3, "User 3" ), 9 ) );

		return result;
	}

}
