package betmen.web.controllers.rest.user.data;

import betmen.core.entity.User;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.SecurityService;
import betmen.core.service.UserService;
import betmen.dto.dto.NewUserDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.edit.UserEditDTO;
import betmen.dto.dto.UserRegResponse;
import betmen.web.converters.DTOService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/rest/users")
public class UserDataRestController {

    private static final Logger LOGGER = Logger.getLogger(UserDataRestController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/current/")
    public UserEditDTO getCurrentUser(final Principal principal) {
        return transformUser(userService.findByLogin(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/")
    public UserEditDTO getUser(final @PathVariable("userId") int userId, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        User user = userService.load(userId);
        assertUserExistsAndCurrent(user, currentUser);
        return transformUser(user);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/create/")
    public UserRegResponse create(final @Validated @RequestBody NewUserDTO newUserDTO) {
        User user = userService.createUser(newUserDTO.getLogin(), newUserDTO.getName(), newUserDTO.getPassword());
        UserDTO userDTO = dtoService.transformUser(user);
        UserRegResponse userRegResponse = new UserRegResponse(true);
        userRegResponse.setUser(userDTO);
        return userRegResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/")
    public UserEditDTO saveUserData(@Valid @RequestBody final UserEditDTO dto, final Principal principal) {
        final User user = userService.load(dto.getUserId());
        final User currentUser = userService.findByLogin(principal.getName());
        assertUserExistsAndCurrent(user, currentUser);
        user.setUsername(dto.getUserName());
        userService.save(user);
        return dto;
    }

    private void assertUserExistsAndCurrent(final User user, final User currentUser) {
        if (user == null) {
            throw new UnprocessableEntityException("User not found");
        }
        if (!currentUser.equals(user)) {
            LOGGER.warn(String.format("User %s is trying to edit data of user %s", currentUser, user));
            throw new UnprocessableEntityException("User not found");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/is-admin/")
    public boolean isAdmin(@PathVariable("userId") final int userId) {
        return securityService.isAdmin(userId);
    }

    private UserEditDTO transformUser(final User user) {
        final UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUserId(user.getId());
        userEditDTO.setLogin(user.getLogin());
        userEditDTO.setUserName(user.getUsername());
        return userEditDTO;
    }
}
