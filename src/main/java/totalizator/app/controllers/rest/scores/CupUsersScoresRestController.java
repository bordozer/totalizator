package totalizator.app.controllers.rest.scores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.points.CupPointsService;
import totalizator.app.services.points.UserRatingService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/rest/cups/{cupId}" )
public class CupUsersScoresRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private CupPointsService cupPointsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserRatingService userRatingService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/scores/" )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( dtoService.transformUser( currentUser ) );

		result.setUserRatingPositions( getUsersCupPoints( cup, userGroupId )
				.stream()
				.map( new Function<UserCupPointsHolder, UserRatingPositionDTO>() {

					@Override
					public UserRatingPositionDTO apply( final UserCupPointsHolder userCupPointsHolder ) {
						return new UserRatingPositionDTO( dtoService.transformUser( userCupPointsHolder.getUser() ), dtoService.transformCupPoints( userCupPointsHolder ) );
					}
				} )
				.collect( Collectors.toList() ) );

		return result;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/scores/in-time/" )
	public CupUsersScoresInTimeDTO cupUsersScoresInTime( final @PathVariable( "cupId" ) int cupId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId ) {
		return userRatingService.cupUsersScoresInTime( cupId, userGroupId );
	}

	private List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final int userGroupId ) {

		if ( userGroupId == 0 ) {
			return cupPointsService.getUsersCupPoints( cup );
		}

		return cupPointsService.getUsersCupPoints( cup, userGroupService.load( userGroupId ) );
	}
}
