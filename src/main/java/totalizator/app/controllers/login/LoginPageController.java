package totalizator.app.controllers.login;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "login" )
public class LoginPageController {

	private final Logger LOGGER = Logger.getLogger( LoginPageController.class );

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET )
	public LoginDTO getDefaultLogin(  ) {
		return new LoginDTO();
	}

	@RequestMapping( method = RequestMethod.PUT, value = "", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LoginDTO doLogin( final @RequestBody LoginDTO loginDTO ) {
		return loginDTO;
	}
}
