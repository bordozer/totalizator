package totalizator.app.controllers.ui.admin.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "admin/games-data-import" )
public class GamesDataImportController {

	public static final String MODEL_NAME = "gamesDataImportModel";

	private static final String VIEW = "/admin/GamesDataImport";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public GamesDataImportModel preparePagingModel() {
		return new GamesDataImportModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String main( final @ModelAttribute( MODEL_NAME ) GamesDataImportModel model ) {
		return VIEW;
	}
}
