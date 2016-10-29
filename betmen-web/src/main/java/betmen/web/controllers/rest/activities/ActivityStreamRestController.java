package betmen.web.controllers.rest.activities;

import betmen.core.entity.User;
import betmen.core.entity.activities.ActivityStreamEntryType;
import betmen.core.service.UserService;
import betmen.core.service.activiries.ActivityStreamService;
import betmen.dto.dto.ActivityStreamDTO;
import betmen.web.converters.ActivitiesDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/rest/activity-stream")
public class ActivityStreamRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private ActivitiesDTOService activitiesDTOService;

    @RequestMapping(method = RequestMethod.GET, value = "/portal/")
    public List<ActivityStreamDTO> portalPageActivities(final Principal principal) {
        return activitiesDTOService.transformActivities(activityStreamService.loadAllForLast(24), getCurrentUser(principal), newArrayList(ActivityStreamEntryType.MATCH_FINISHED));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/matches/{matchId}/")
    public List<ActivityStreamDTO> matchActivities(@PathVariable("matchId") final int matchId, final Principal principal) {
        return activitiesDTOService.transformActivities(activityStreamService.loadAllForMatch(matchId), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/")
    public List<ActivityStreamDTO> userActivities(@PathVariable("userId") final int userId, final Principal principal) {
        return activitiesDTOService.transformActivities(activityStreamService.loadAllForUser(userId, 20), getCurrentUser(principal));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
