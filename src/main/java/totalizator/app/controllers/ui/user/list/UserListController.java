package totalizator.app.controllers.ui.user.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "/totalizator/users" )
public class UserListController {

	public static final String MODEL_NAME = "userListModel";

	private static final String VIEW = "/UserList";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public UserListModel preparePagingModel( final Principal principal ) {
		return new UserListModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) UserListModel model ) {
		return VIEW;
	}
}
