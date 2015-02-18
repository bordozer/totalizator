package totalizator.app.controllers.user.data;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "/users" )
public class UserDataController {

	private static final Logger LOGGER = Logger.getLogger( UserDataController.class );

	@Autowired
	private UserService userService;

	/*@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public UserDTO getDefaultLogin() {
		return new UserDTO();
	}*/

	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.PUT, value = "/create/" )
	public void registerUser( final @RequestBody NewUserDTO newUserDTO ) {
		userService.createUser( newUserDTO.getLogin(), newUserDTO.getName(), newUserDTO.getPassword() );
	}
}
