package totalizator.app.controllers.ui.translator;

import totalizator.app.translator.NerdKey;
import totalizator.app.translator.TranslationData;

import java.util.Map;

public class TranslationsModel {

	private Map<NerdKey, TranslationData> untranslatedMap;

	public void setUntranslatedMap( final Map<NerdKey, TranslationData> untranslatedMap ) {
		this.untranslatedMap = untranslatedMap;
	}

	public Map<NerdKey, TranslationData> getUntranslatedMap() {
		return untranslatedMap;
	}
}
