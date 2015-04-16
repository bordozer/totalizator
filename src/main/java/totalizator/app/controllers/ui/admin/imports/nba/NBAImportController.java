package totalizator.app.controllers.ui.admin.imports.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping( "admin/imports/nba" )
public class NBAImportController {

	public static final String MODEL_NAME = "nbaImportModel";

	private static final String VIEW = "/admin/imports/NBA";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public NBAImportModel preparePagingModel( final Principal principal ) {
		return new NBAImportModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String main( final @ModelAttribute( MODEL_NAME ) NBAImportModel model ) {
		return VIEW;
	}
}
