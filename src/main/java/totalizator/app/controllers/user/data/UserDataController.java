package totalizator.app.controllers.user.data;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "user" )
public class UserDataController {

	private static final Logger LOGGER = Logger.getLogger( UserDataController.class );

	/*@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public UserDTO getDefaultLogin() {
		return new UserDTO();
	}*/

	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.POST, value = "/create/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public void registerUser( final @RequestBody NewUserDTO newUserDTO ) {
		// TODO: create new user
		LOGGER.debug( String.format( "New user: %s, ( name: %s, password: %s )", newUserDTO.getName(), newUserDTO.getLogin(), newUserDTO.getPassword() ) );
	}
}
