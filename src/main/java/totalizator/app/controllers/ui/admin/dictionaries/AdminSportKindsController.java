package totalizator.app.controllers.ui.admin.dictionaries;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "admin/dictionaries" )
public class AdminSportKindsController {

	public static final String MODEL_NAME = "dictionariesModel";

	private static final String VIEW = "/admin/Dictionaries";

	@ModelAttribute( MODEL_NAME )
	public DictionariesModel preparePagingModel() {
		return new DictionariesModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) DictionariesModel model ) {
		return VIEW;
	}
}
