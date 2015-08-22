package totalizator.app.controllers.rest.scores;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.score.UserMatchPointsCalculationService;

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
	private UserMatchPointsCalculationService userMatchPointsCalculationService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private UserGroupService userGroupService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/scores/", produces = APPLICATION_JSON_VALUE )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( dtoService.transformUser( currentUser ) );

		result.setUserRatingPositions( getUsersPoints( cup, userGroupId ) );

		return result;
	}

	private List<UserRatingPositionDTO> getUsersPoints( final Cup cup, final int userGroupId ) {

		return Lists.transform( getUsersCupPoints( cup, userGroupId ), new Function<UserCupPointsHolder, UserRatingPositionDTO>() {

			@Override
			public UserRatingPositionDTO apply( final UserCupPointsHolder userCupPointsHolder ) {
				return new UserRatingPositionDTO( dtoService.transformUser( userCupPointsHolder.getUser() ), dtoService.transformCupPoints( userCupPointsHolder ) );
			}
		} );
	}

	private List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final int userGroupId ) {

		if ( userGroupId == 0 ) {
			return userMatchPointsCalculationService.getUsersCupPoints( cup );
		}

		return userMatchPointsCalculationService.getUsersCupPoints( cup, userGroupService.load( userGroupId ) );
	}
}
