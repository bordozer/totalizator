package totalizator.app.controllers.ui.portal;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
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

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@ModelAttribute( MODEL_NAME )
	public PortalPageModel preparePagingModel( final Principal principal ) {
		return new PortalPageModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) PortalPageModel model, final Principal principal ) {

		model.setCupsToShow( dtoService.transformCups( cupService.portalPageCups(), userService.findByLogin( principal.getName() ) ) );

		model.setCupsToShowJSON( new Gson().toJson( model.getCupsToShow() ) );

		return VIEW;
	}
}
