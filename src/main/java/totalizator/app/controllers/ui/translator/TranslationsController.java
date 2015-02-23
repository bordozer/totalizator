package totalizator.app.controllers.ui.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.translator.TranslatorService;

@Controller
@RequestMapping( "/translations" )
public class TranslationsController {

	public static final String MODEL_NAME = "translationsModel";

	private static final String VIEW = "/Translations";

	@Autowired
	private TranslatorService translatorService;

	@ModelAttribute( MODEL_NAME )
	public TranslationsModel preparePagingModel() {
		return new TranslationsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String getDefaultLogin( final @ModelAttribute( MODEL_NAME ) TranslationsModel model ) {

		model.setUntranslatedMap( translatorService.getUntranslatedMap() );

		return VIEW;
	}
}
