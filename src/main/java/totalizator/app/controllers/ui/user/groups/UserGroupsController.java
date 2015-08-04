package totalizator.app.controllers.ui.user.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "/totalizator/users/{userId}/groups" )
public class UserGroupsController {

	public static final String MODEL_NAME = "userGroupsModel";

	private static final String VIEW = "/UserGroups";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public UserGroupsModel preparePagingModel() {
		return new UserGroupsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String list( final @PathVariable( "userId" ) int userId, final @ModelAttribute( MODEL_NAME ) UserGroupsModel model ) {
		model.setUser( userService.load( userId ) );
		return VIEW;
	}
}
