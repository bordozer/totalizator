package totalizator.app.controllers.rest.translator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TranslatorModel {

	private List<TranslationDTO> translations;

	public List<TranslationDTO> getTranslations() {
		return translations;
	}

	public void setTranslations( final List<TranslationDTO> translations ) {
		this.translations = translations;
	}
}
