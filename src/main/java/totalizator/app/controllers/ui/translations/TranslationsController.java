package totalizator.app.controllers.ui.translations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "/translations" )
public class TranslationsController {

	public static final String MODEL_NAME = "translationsModel";

	private static final String VIEW = "/Translations";

	@ModelAttribute( MODEL_NAME )
	public TranslationsModel preparePagingModel() {
		return new TranslationsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String getDefaultLogin( final @ModelAttribute( MODEL_NAME ) TranslationsModel model ) {

		return VIEW;
	}
}
