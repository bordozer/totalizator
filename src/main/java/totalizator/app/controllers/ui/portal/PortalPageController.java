package totalizator.app.controllers.ui.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.config.root.SecurityConfig;

@Controller
@RequestMapping( SecurityConfig.PORTAL_PAGE_URL )
public class PortalPageController {

	private static final String VIEW = "/PortalPage";

	@RequestMapping( method = RequestMethod.GET, value = "" )
	public String doLogin() {
		return VIEW;
	}
}
