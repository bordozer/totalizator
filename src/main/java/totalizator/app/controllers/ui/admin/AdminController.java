package totalizator.app.controllers.ui.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "admin" )
public class AdminController {

	public static final String MODEL_NAME = "adminModel";

	private static final String VIEW_MAIN_PAGE = "/admin/AdminPage";
	private static final String VIEW_TRANSLATIONS = "/admin/Translations";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public AdminModel preparePagingModel() {
		return new AdminModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String main( final @ModelAttribute( MODEL_NAME ) AdminModel model ) {
		return VIEW_MAIN_PAGE;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/translations/" )
	public String translations( final @ModelAttribute( MODEL_NAME ) AdminModel model ) {
		return VIEW_TRANSLATIONS;
	}
}
