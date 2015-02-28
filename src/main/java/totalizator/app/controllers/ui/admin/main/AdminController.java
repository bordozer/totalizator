package totalizator.app.controllers.ui.admin.main;

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

	private static final String VIEW_MAIN_PAGE = "/admin/AdminPage";
	private static final String VIEW_MATCHES = "/admin/AdminMatches";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public AdminModel preparePagingModel() {
		return new AdminModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String doLogin( final Principal principal, final @ModelAttribute( MODEL_NAME ) AdminModel model ) {

		final User user = getUserByLogin( principal );

		model.setUserName( user.getUsername() );

		return VIEW_MAIN_PAGE;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/matches/" )
	public String matches( final Principal principal, final @ModelAttribute( MODEL_NAME ) AdminModel model ) {

		final User user = getUserByLogin( principal );

		model.setUserName( user.getUsername() );

		return VIEW_MATCHES;
	}

	private User getUserByLogin( final Principal principal ) {
		return userService.findUserByLogin( principal.getName() );
	}
}
