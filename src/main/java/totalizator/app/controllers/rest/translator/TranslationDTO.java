package totalizator.app.controllers.rest.translator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TranslationDTO {

	private Map<String, String> translations;

	public TranslationDTO() {
	}

	public TranslationDTO( final Map<String, String> translations ) {
		this.translations = translations;
	}

	public Map<String, String> getTranslations() {
		return translations;
	}

	public void setTranslations( final Map<String, String> translations ) {
		this.translations = translations;
	}
}
