package totalizator.app.controllers.rest.admin.translations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TranslationsDTO {

	private String nerd;
	private List<TranslationEntryDTO> translationEntries;

	public void setNerd( final String nerd ) {
		this.nerd = nerd;
	}

	public String getNerd() {
		return nerd;
	}

	public void setTranslationEntries( final List<TranslationEntryDTO> translationEntries ) {
		this.translationEntries = translationEntries;
	}

	public List<TranslationEntryDTO> getTranslationEntries() {
		return translationEntries;
	}
}
