package totalizator.app.translator;

import org.dom4j.DocumentException;

import java.util.Map;

public interface TranslatorService {

	String translate( final String nerd, final Language language, final String... params );

	Map<NerdKey, TranslationData> getTranslationsMap();

	Map<NerdKey, TranslationData> getUntranslatedMap();

	Map<NerdKey, TranslationData> getUnusedTranslationsMap();

	void reloadTranslations() throws DocumentException;
}
