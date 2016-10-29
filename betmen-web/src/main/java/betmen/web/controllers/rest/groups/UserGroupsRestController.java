package betmen.web.controllers.rest.groups;

import betmen.core.entity.User;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserGroupDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/user-groups/")
public class UserGroupsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UserGroupDTO> allUserGroups(final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadAll(), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userGroupId}/members/")
    public List<UserDTO> allMembersOfUserGroup(@PathVariable("userGroupId") final int userGroupId) {
        return dtoService.transformUsers(userGroupService.loadUserGroupMembers(userGroupService.load(userGroupId)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{userGroupId}/members/{userId}/add/")
    public void addMember(@PathVariable("userGroupId") final int userGroupId, @PathVariable("userId") final int userId) {
        userGroupService.addMember(userGroupService.load(userGroupId), userService.load(userId));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userGroupId}/members/{userId}/remove/")
    public void removeMember(@PathVariable("userGroupId") final int userGroupId, @PathVariable("userId") final int userId) {
        userGroupService.removeMember(userGroupService.load(userGroupId), userService.load(userId));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
