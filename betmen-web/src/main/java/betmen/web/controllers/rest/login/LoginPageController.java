package betmen.web.controllers.rest.login;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/login")
public class LoginPageController {

    private final Logger LOGGER = Logger.getLogger(LoginPageController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public LoginDTO doLogin(@RequestBody final LoginDTO loginDTO) {
        return loginDTO;
    }
}
