package betmen.web.controllers.rest.admin;

import betmen.core.entity.User;
import betmen.core.service.UserService;
import betmen.dto.dto.admin.AdminMainPageDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/admin/rest")
public class AdminMainPageRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public AdminMainPageDTO adminMainPage(final Principal principal) {

        final User user = userService.findByLogin(principal.getName());

        final AdminMainPageDTO dto = new AdminMainPageDTO();
        dto.setUserDTO(dtoService.transformUser(user));

        return dto;
    }
}
