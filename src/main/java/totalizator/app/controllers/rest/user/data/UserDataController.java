package totalizator.app.controllers.rest.user.data;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/users" )
public class UserDataController {

	private static final Logger LOGGER = Logger.getLogger( UserDataController.class );

	@Autowired
	private UserService userService;

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<UserDTO> getUsers() {
		return Lists.transform( userService.loadAll(), this::getUserDTO );
	}

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/current/", produces = APPLICATION_JSON_VALUE )
	public UserDTO getCurrentUser( final Principal principal ) {
		return getUserDTO( userService.findByName( principal.getName() ) );
	}

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public UserDTO getUser( final @PathVariable( "userId" ) int userId ) {
		return getUserDTO( userService.load( userId ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/create/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public NewUserDTO registerUser( final @RequestBody NewUserDTO newUserDTO ) {
		userService.createUser( newUserDTO.getLogin(), newUserDTO.getName(), newUserDTO.getPassword() );
		return newUserDTO;
	}

	private UserDTO getUserDTO( final User user ) {
		final UserDTO userDTO = new UserDTO();
		userDTO.setLogin( user.getLogin() );
		userDTO.setName( user.getUsername() );
		return userDTO;
	}
}
