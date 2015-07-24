package totalizator.app.controllers.ui.user.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "/totalizator/users/{userId}/settings" )
public class UserSettingsController {

	public static final String MODEL_NAME = "userSettingsModel";

	private static final String VIEW = "/UserSettings";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public UserSettingsModel prepareModel( final Principal principal ) {
		return new UserSettingsModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String get( final @PathVariable( "userId" ) int userId, final @ModelAttribute( MODEL_NAME ) UserSettingsModel model ) {

		model.setUser( userService.load( userId ) );

		return VIEW;
	}
}
