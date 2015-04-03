package totalizator.app.controllers.ui.user.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "totalizator/users/{userId}/" )
public class UserCardController {

	public static final String MODEL_NAME = "userCardModel";

	private static final String VIEW = "/UserCard";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public UserCardModel preparePagingModel( final Principal principal, final @PathVariable( "userId" ) int userId ) {

		final UserCardModel model = new UserCardModel( userService.load( userId ) );

		final User user = userService.findByLogin( principal.getName() );
		model.setUserName( user.getUsername() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) UserCardModel model ) {



		return VIEW;
	}
}
