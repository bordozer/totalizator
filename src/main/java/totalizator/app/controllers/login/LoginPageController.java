package totalizator.app.controllers.login;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping( "login" )
public class LoginPageController {

	private final Logger LOGGER = Logger.getLogger( LoginPageController.class );

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET )
	public LoginDTO getDefaultLogin() {
		return new LoginDTO();
	}
}
