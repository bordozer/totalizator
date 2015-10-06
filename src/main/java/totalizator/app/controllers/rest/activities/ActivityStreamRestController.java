package totalizator.app.controllers.rest.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.models.User;
import totalizator.app.models.activities.ActivityStreamEntryType;
import totalizator.app.services.UserService;
import totalizator.app.services.activiries.ActivitiesDTOService;
import totalizator.app.services.activiries.ActivityStreamService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping( "/rest/activity-stream" )
public class ActivityStreamRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private ActivitiesDTOService activitiesDTOService;

	@RequestMapping( method = RequestMethod.GET, value = "/portal/" )
	public List<ActivityStreamDTO> portalPageActivities( final Principal principal ) {
		return activitiesDTOService.transformActivities( activityStreamService.loadAllForLast( 24 ), getCurrentUser( principal ), newArrayList( ActivityStreamEntryType.MATCH_FINISHED ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/matches/{matchId}/" )
	public List<ActivityStreamDTO> matchActivities( final @PathVariable( "matchId" ) int matchId, final Principal principal ) {
		return activitiesDTOService.transformActivities( activityStreamService.loadAllForMatch( matchId ), getCurrentUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/users/{userId}/" )
	public List<ActivityStreamDTO> userActivities( final @PathVariable( "userId" ) int userId, final Principal principal ) {
		return activitiesDTOService.transformActivities( activityStreamService.loadAllForUser( userId ), getCurrentUser( principal ) );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
