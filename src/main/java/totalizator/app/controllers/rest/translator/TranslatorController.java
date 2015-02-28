package totalizator.app.controllers.rest.translator;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/translator" )
public class TranslatorController {

	@Autowired
	private TranslatorService translatorService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public TranslationDTO getDefaultLogin( final TranslationDTO dto ) {

		final Map<String, String> translations = dto.getTranslations();

		return new TranslationDTO( Maps.transformValues( translations, new Function<String, String>() {
			@Override
			public String apply( final String nerd ) {
				return translate( nerd );
			}
		} ) );
	}

	private String translate( final String text ) {
		return translatorService.translate( text, Language.RU );
	}
}
