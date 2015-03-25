package totalizator.app.controllers.ui.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "/totalizator/cups" )
public class CupController {

	public static final String MODEL_NAME = "cupModel";

	private static final String VIEW = "/Cup";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public CupModel preparePagingModel() {
		return new CupModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/" )
	public String portalPage( final Principal principal, final @PathVariable( "cupId" ) int cupId, final @ModelAttribute( MODEL_NAME ) CupModel model ) {

		final User user = userService.findByLogin( principal.getName() );
		model.setUserName( user.getUsername() );
		model.setCupId( cupId );

		return VIEW;
	}
}
