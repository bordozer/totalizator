package betmen.web.controllers.rest.user.groups;

import betmen.core.entity.AbstractEntity;
import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.dto.dto.UserGroupDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @RequestMapping(method = RequestMethod.GET, value = "/member/")
    public List<UserGroupDTO> loadUserGroupsWhereUserIsMember(@PathVariable("userId") final int userId, final Principal principal) {
        return dtoService.transformUserGroups(userGroupService.loadUserGroupsWhereUserIsMember(userService.load(userId)), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UserGroupEditDTO> userGroupList(final @PathVariable("userId") int userId) {
        final List<UserGroup> userGroups = userGroupService.loadUserGroupsWhereUserIsOwner(userService.load(userId));
        final Function<UserGroup, UserGroupEditDTO> mapper = userGroup -> {
            final UserGroupEditDTO dto = new UserGroupEditDTO();
            dto.setUserGroupId(userGroup.getId());
            dto.setUserGroupName(userGroup.getGroupName());
            dto.setCupIds(userGroupService.loadCups(userGroup).stream().map(AbstractEntity::getId).collect(Collectors.toList()));
            return dto;
        };
        return userGroups.stream().map(mapper).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE)
    public UserGroupEditDTO create(@RequestBody final UserGroupEditDTO dto, @PathVariable("userId") final int userId) {
        // TODO: validate!
        final UserGroup userGroup = save(userId, dto, new UserGroup());
        dto.setUserGroupId(userGroup.getId());
        return dto;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value = "/{userGroupId}", produces = APPLICATION_JSON_VALUE)
    public UserGroupEditDTO save(@RequestBody final UserGroupEditDTO dto, @PathVariable("userId") final int userId, @PathVariable("userGroupId") final int userGroupId) {
        // TODO: validate!
        save(userId, dto, userGroupService.load(userGroupId));
        return dto;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userGroupId}")
    public void delete(@PathVariable("userId") final int userId, @PathVariable("userGroupId") final int userGroupId) {
        userGroupService.delete(userGroupId);
    }

    private UserGroup save(final int userId, final UserGroupEditDTO dto, final UserGroup userGroup) {
        final User groupOwner = userService.load(userId);
        userGroup.setOwner(groupOwner);
        userGroup.setGroupName(dto.getUserGroupName());
        return userGroupService.save(userGroup, dto.getCupIds());
    }

    private User getUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
