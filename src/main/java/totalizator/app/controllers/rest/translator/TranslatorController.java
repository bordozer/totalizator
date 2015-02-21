package totalizator.app.controllers.rest.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.services.TranslatorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/translator" )
public class TranslatorController {

	@Autowired
	private TranslatorService translatorService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public TranslatorModel getDefaultLogin( final @RequestBody TranslatorModel model ) {

		for ( final TranslationDTO dto : model.getTranslations() ) {
			dto.setText( translatorService.translate( dto.getText() ) );
		}

		return model;
	}
}
