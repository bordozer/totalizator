package totalizator.app.controllers.portal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/portal-page" )
public class PortalPageController {

	private static final Logger LOGGER = Logger.getLogger( PortalPageController.class );

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public PortalPageDTO getDefaultLogin( final @RequestBody PortalPageDTO portalPageDTO ) {

		final User user = userService.findUserByLogin( "hakeem" );

		portalPageDTO.setUserName( user.getUsername() );

		return portalPageDTO;
	}
}
