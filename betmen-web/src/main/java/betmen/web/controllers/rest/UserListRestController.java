package betmen.web.controllers.rest;

import betmen.core.service.UserService;
import betmen.dto.dto.UserDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserListRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UserDTO> cupUsersScores() {
        return dtoService.transformUsers(userService.loadAll());
    }
}
