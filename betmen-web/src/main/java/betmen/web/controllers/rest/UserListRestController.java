package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserListItemDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/users")
public class UserListRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private MatchBetsService matchBetsService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UserDTO> getAllUsers() {
        return dtoService.transformUsers(userService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/")
    public List<UserListItemDTO> userListPage() {
        List<User> users = userService.loadAll();
        return users.stream()
                .map(user -> {
                    UserListItemDTO dto = new UserListItemDTO();
                    dto.setUser(dtoService.transformUser(user));
                    dto.setBetsCount(matchBetsService.userBetsCount(user));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
