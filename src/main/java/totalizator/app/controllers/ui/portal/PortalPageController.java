package totalizator.app.controllers.ui.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "resources" )
public class PortalPageController {

	private static final String VIEW = "/PortalPage";

	@RequestMapping( method = RequestMethod.GET, value = "/totalizator.html" )
	public String doLogin() {
		return VIEW;
	}
}
