package totalizator.app.controllers.ui.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "admin" )
public class AdminController {

	public static final String MODEL_NAME = "adminModel";

	private static final String VIEW_MAIN_PAGE = "/admin/AdminPage";
	private static final String VIEW_MATCHES = "/admin/AdminMatches";
	private static final String VIEW_TRANSLATIONS = "/admin/Translations";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public AdminModel preparePagingModel( final Principal principal ) {
		return new AdminModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String doLogin( final @ModelAttribute( MODEL_NAME ) AdminModel model ) {
		return VIEW_MAIN_PAGE;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/matches/" )
	public String matches( final @ModelAttribute( MODEL_NAME ) AdminModel model ) {
		return VIEW_MATCHES;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/translations/" )
	public String getDefaultLogin( final @ModelAttribute( MODEL_NAME ) AdminModel model ) {
		return VIEW_TRANSLATIONS;
	}
}
