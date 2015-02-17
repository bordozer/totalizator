package totalizator.app.controllers.user.data;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/user" )
public class UserDataController {

	private static final Logger LOGGER = Logger.getLogger( UserDataController.class );

	/*@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public UserDTO getDefaultLogin() {
		return new UserDTO();
	}*/

	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.POST, value = "/create/" )
	public void registerUser( final @RequestBody NewUserDTO newUserDTO ) {
		// TODO: create new user
		LOGGER.debug( String.format( "New user: %s, ( name: %s, password: %s )", newUserDTO.getName(), newUserDTO.getLogin(), newUserDTO.getPassword() ) );
	}
}
