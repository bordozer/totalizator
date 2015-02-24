package totalizator.app.controllers.rest.translations;

import java.util.List;

public class TranslationsModel {

	private List<TranslationsDTO> untranslatedList;

	public List<TranslationsDTO> getUntranslatedList() {
		return untranslatedList;
	}

	public void setUntranslatedList( final List<TranslationsDTO> untranslatedList ) {
		this.untranslatedList = untranslatedList;
	}
}
