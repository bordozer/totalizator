package totalizator.app.controllers.ui.admin.controlPanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "admin/control-panel" )
public class ControlPanelController {

	private static final String VIEW = "/admin/ControlPanel";

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String portalPage() {
		return VIEW;
	}
}
