package totalizator.app.controllers.ui.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "admin" )
public class AdminController {

	private static final String VIEW = "/AdminPage";

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String doLogin() {
		return VIEW;
	}
}
