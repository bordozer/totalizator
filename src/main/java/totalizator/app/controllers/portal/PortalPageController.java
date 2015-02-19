package totalizator.app.controllers.portal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public PortalPageDTO getDefaultLogin() {

		final User user = userService.findUserByLogin( "hakeem" );

		final PortalPageDTO portalPageDTO = new PortalPageDTO();
		portalPageDTO.setUserName( user.getUsername() );

		return portalPageDTO;
	}
}
