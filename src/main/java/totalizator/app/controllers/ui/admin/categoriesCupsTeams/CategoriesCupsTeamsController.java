package totalizator.app.controllers.ui.admin.categoriesCupsTeams;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "admin/categories-cups-teams" )
public class CategoriesCupsTeamsController {

	private static final String VIEW = "/admin/CategoriesCupsTeams";

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String adminPage() {
		return VIEW;
	}
}
