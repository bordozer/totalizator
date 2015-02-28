package totalizator.app.controllers.rest.login;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/login" )
public class LoginPageController {

	private final Logger LOGGER = Logger.getLogger( LoginPageController.class );

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LoginDTO doLogin( final @RequestBody LoginDTO loginDTO ) {
		return loginDTO;
	}
}
