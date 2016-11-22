package betmen.web.controllers.rest.groups;

import betmen.core.entity.User;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.dto.dto.UserGroupDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/users/{userId}/groups")
public class User_UserGroupsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/owner/")
    public List<UserGroupDTO> loadUserGroupsWhereUserIsOwner(@PathVariable("userId") final int userId, final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadUserGroupsWhereUserIsOwner(userService.load(userId)), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/owner/?cup={cupId}")
    public List<UserGroupDTO> loadUserGroupsForCupWhereUserIsOwner(@PathVariable("userId") final int userId, @RequestParam("cupId") final int cupId, final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadUserGroupsWhereUserIsOwner(userService.load(userId), cupId), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/")
    public List<UserGroupDTO> loadUserGroupsWhereUserIsMember(@PathVariable("userId") final int userId, final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadUserGroupsWhereUserIsMember(userService.load(userId)), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/?cup={cupId}")
    public List<UserGroupDTO> loadUserGroupsForCupWhereUserIsMember(@PathVariable("userId") final int userId, @RequestParam("cupId") final int cupId, final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadUserGroupsWhereUserIsMember(userService.load(userId), cupId), getUser(principal));
    }

    private User getUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
