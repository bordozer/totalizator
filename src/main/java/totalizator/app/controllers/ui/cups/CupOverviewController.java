package totalizator.app.controllers.ui.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.CupService;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "/totalizator/cups" )
public class CupOverviewController {

	public static final String MODEL_NAME = "cupOverviewModel";

	private static final String VIEW = "/CupOverview";

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@ModelAttribute( MODEL_NAME )
	public CupOverviewModel preparePagingModel( final Principal principal ) {
		return new CupOverviewModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/" )
	public String portalPage( final @PathVariable( "cupId" ) int cupId, final @ModelAttribute( MODEL_NAME ) CupOverviewModel model ) {

		model.setCup( cupService.load( cupId ) );

		return VIEW;
	}
}
