package betmen.web.controllers.rest;

import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.model.points.UserCupPointsHolder;
import betmen.core.service.CupService;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.core.service.points.CupPointsService;
import betmen.dto.dto.points.scores.CupUsersScoresDTO;
import betmen.dto.dto.points.scores.CupUsersScoresInTimeDTO;
import betmen.dto.dto.points.scores.UserRatingPositionDTO;
import betmen.web.converters.DTOService;
import betmen.web.converters.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/cups/{cupId}")
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

    @RequestMapping(method = RequestMethod.GET, value = "/scores/")
    public CupUsersScoresDTO cupUsersScores(final @PathVariable("cupId") int cupId, final @RequestParam(value = "userGroupId", required = false) Integer userGroupId, final Principal principal) {

        final User currentUser = userService.findByLogin(principal.getName());
        final Cup cup = cupService.loadAndAssertExists(cupId);

        final CupUsersScoresDTO result = new CupUsersScoresDTO();

        result.setCurrentUser(dtoService.transformUser(currentUser));

        result.setUserRatingPositions(getUsersCupPoints(cup, getUserGroupId(userGroupId))
                .stream()
                .map(userCupPointsHolder -> new UserRatingPositionDTO(dtoService.transformUser(userCupPointsHolder.getUser()), dtoService.transformCupPoints(userCupPointsHolder)))
                .collect(Collectors.toList()));

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scores/in-time/")
    public CupUsersScoresInTimeDTO cupUsersScoresInTime(final @PathVariable("cupId") int cupId, final @RequestParam(value = "userGroupId", required = false) Integer userGroupId) {
        return userRatingService.cupUsersScoresInTime(cupId, getUserGroupId(userGroupId));
    }

    private Integer getUserGroupId(final Integer userGroupId) {
        return Optional.ofNullable(userGroupId).map(groupId -> groupId).orElse(0);
    }

    private List<UserCupPointsHolder> getUsersCupPoints(final Cup cup, final int userGroupId) {

        if (userGroupId == 0) {
            return cupPointsService.getUsersCupPoints(cup);
        }

        return cupPointsService.getUsersCupPoints(cup, userGroupService.load(userGroupId));
    }
}
