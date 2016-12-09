package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.service.UserService;
import betmen.dto.dto.NewUserDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserRegResponse;
import betmen.dto.edit.UserEditDTO;
import betmen.web.converters.DTOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/rest/users")
public class UserDataRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.PUT, value = "/create/")
    public UserRegResponse create(@Validated @RequestBody final NewUserDTO newUserDTO) {
        User user = userService.createUser(newUserDTO.getLogin(), newUserDTO.getName(), newUserDTO.getPassword());
        UserDTO userDTO = dtoService.transformUser(user);
        UserRegResponse userRegResponse = new UserRegResponse(true);
        userRegResponse.setUser(userDTO);
        return userRegResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/settings/")
    public UserEditDTO getUserSettings(final Principal principal) {
        return toEditDto(userService.findByLogin(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/settings/")
    public UserEditDTO modifyUserSettings(@Validated @RequestBody final UserEditDTO dto, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        currentUser.setUsername(dto.getUserName());
        userService.save(currentUser);
        return dto;
    }

    private UserEditDTO toEditDto(final User user) {
        final UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setUserId(user.getId());
        userEditDTO.setLogin(user.getLogin());
        userEditDTO.setUserName(user.getUsername());
        return userEditDTO;
    }
}
