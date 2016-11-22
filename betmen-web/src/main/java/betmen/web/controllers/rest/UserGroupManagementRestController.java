package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.model.ErrorCodes;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.dto.dto.UserGroupEditDTO;
import betmen.web.converters.UserGroupConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/rest/user-groups/management")
public class UserGroupManagementRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private UserGroupConverter userGroupConverter;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UserGroupEditDTO> userGroupList(final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final List<UserGroupEntity> userGroupEntities = userGroupService.loadUserGroupsWhereUserIsOwner(userService.load(currentUser.getId()));
        return userGroupEntities.stream().map(entity -> userGroupConverter.convertToDto(entity)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE)
    public UserGroupEditDTO createUserGroup(@Validated @RequestBody final UserGroupEditDTO dto, final Principal principal) {
        final UserGroupEntity entity = new UserGroupEntity();
        return persist(entity, dto, getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userGroupId}", produces = APPLICATION_JSON_VALUE)
    public UserGroupEditDTO editUserGroup(@Validated @RequestBody final UserGroupEditDTO dto, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final int userGroupId = dto.getUserGroupId();
        final UserGroupEntity entity = userGroupService.load(userGroupId);
        if (entity == null || !entity.getOwner().equals(currentUser)) {
            throw new UnprocessableEntityException(ErrorCodes.USER_GROUP_NOT_FOUND);
        }
        entity.setUserGroupMembers(entity.getUserGroupMembers());
        return persist(entity, dto, currentUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userGroupId}")
    public boolean delete(@PathVariable("userGroupId") final int userGroupId, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final UserGroupEntity entity = userGroupService.load(userGroupId);
        if (entity == null || !entity.getOwner().equals(currentUser)) {
            throw new UnprocessableEntityException(ErrorCodes.USER_GROUP_NOT_FOUND);
        }
        userGroupService.delete(userGroupId);
        return true;
    }

    private UserGroupEditDTO persist(final UserGroupEntity entity, final UserGroupEditDTO dto, final User currentUser) {
        userGroupConverter.populateEntity(entity, dto, currentUser);
        return userGroupConverter.convertToDto(userGroupService.save(entity));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
