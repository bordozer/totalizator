package betmen.web.controllers.rest;

import betmen.dto.dto.LoginDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/login")
public class LoginPageController {

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public LoginDTO doLogin(@RequestBody final LoginDTO loginDTO) {
        return loginDTO;
    }
}
