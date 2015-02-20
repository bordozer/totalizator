package totalizator.app.controllers.ui.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.User;
import totalizator.app.services.UserService;
import totalizator.config.root.SecurityConfig;

import java.security.Principal;

@Controller
@RequestMapping( SecurityConfig.PORTAL_PAGE_URL )
public class PortalPageController {

	public static final String MODEL_NAME = "portalPageModel";

	private static final String VIEW = "/PortalPage";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public PortalPageModel preparePagingModel() {
		return new PortalPageModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "" )
	public String portalPage( final Principal principal, final @ModelAttribute( MODEL_NAME ) PortalPageModel model ) {

		final User user = userService.findUserByLogin( principal.getName() );

		model.setUserName( user.getUsername() );

		return VIEW;
	}
}
