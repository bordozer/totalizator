package totalizator.app.controllers.rest.user.data;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.User;
import totalizator.app.services.SecurityService;
import totalizator.app.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/users" )
public class UserDataRestController {

	private static final Logger LOGGER = Logger.getLogger( UserDataRestController.class );

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/current/", produces = APPLICATION_JSON_VALUE )
	public UserEditDTO getCurrentUser( final Principal principal ) {
		return transformUser( userService.findByLogin( principal.getName() ) );
	}

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public UserEditDTO getUser( final @PathVariable( "userId" ) int userId ) {
		return transformUser( userService.load( userId ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/create/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public NewUserDTO registerUser( final @RequestBody NewUserDTO newUserDTO ) {
		userService.createUser( newUserDTO.getLogin(), newUserDTO.getName(), newUserDTO.getPassword() );
		return newUserDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{userId}/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public UserEditDTO saveUserData( @Valid @RequestBody final UserEditDTO dto ) {

		final User user = userService.load( dto.getUserId() );
		user.setUsername( dto.getUserName() );

		userService.save( user );

		return dto;
	}

	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/is-admin/", produces = APPLICATION_JSON_VALUE )
	public boolean isAdmin( final @PathVariable( "userId" ) int userId ) {
		return securityService.isAdmin( userId );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( value = HttpStatus.BAD_REQUEST )
	public Error handleException( final MethodArgumentNotValidException exception ) {
		return new Error( "Validation failed" );
	}

	private UserEditDTO transformUser( final User user ) {

		final UserEditDTO userEditDTO = new UserEditDTO();
		userEditDTO.setUserId( user.getId() );
		userEditDTO.setLogin( user.getLogin() );
		userEditDTO.setUserName( user.getUsername() );

		return userEditDTO;
	}
}
