package totalizator.app.controllers.rest.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.translator.NerdKey;
import totalizator.app.translator.TranslationEntry;
import totalizator.app.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/translations" )
public class TranslationsRestController {

	@Autowired
	private TranslatorService translatorService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public TranslationsModel getDefaultLogin() {

		final TranslationsModel model = new TranslationsModel();

		final List<TranslationsDTO> untranslatedList = newArrayList();
		for ( final NerdKey nerdKey : translatorService.getUntranslatedMap().keySet() ) {

			final TranslationsDTO translationsDTO = new TranslationsDTO();
			translationsDTO.setNerd( nerdKey.getNerd() );

			final List<TranslationEntryDTO> untranslated = newArrayList();
			for ( final TranslationEntry translationEntry : translatorService.getUntranslatedMap().get( nerdKey ).getTranslations() ) {
				untranslated.add( new TranslationEntryDTO( translationEntry.getLanguage().getName(), translationEntry.getNerd() ) );
			}
			translationsDTO.setTranslationEntries( untranslated );

			untranslatedList.add( translationsDTO );
		}

		model.setUntranslatedList( untranslatedList );

		return model;
	}
}
