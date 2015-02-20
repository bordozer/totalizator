package totalizator.app.controllers.ui.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "admin" )
public class AdminController {

	public static final String MODEL_NAME = "adminModel";

	private static final String VIEW = "/AdminPage";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public AdminModel preparePagingModel() {
		return new AdminModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String doLogin( final Principal principal, final @ModelAttribute( MODEL_NAME ) AdminModel model ) {

		final User user = userService.findUserByLogin( principal.getName() );

		model.setUserName( user.getUsername() );

		return VIEW;
	}
}
