package totalizator.app.translator;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Service
public class TranslatorServiceImpl implements TranslatorService {

	public static final String TRANSLATIONS_PATH = "src/main/resources/translations/";

	private static final Language DEFAULT_LANGUAGE = Language.EN;

	private Translator translator;

	private static final Logger LOGGER = Logger.getLogger( TranslatorServiceImpl.class );

	@Override
	public String translate( final String nerd, final Language language, final String... params ) {

		if ( nerd.trim().length() == 0 ) {
			return nerd;
		}

		if ( language == Language.NERD ) {
			return nerd;
		}

		final TranslationEntry translationEntry = translator.getTranslation( nerd, language );

		if ( translationEntry instanceof TranslationEntryMissed ) {
			translator.registerNotTranslationEntry( translationEntry );
		}

		String result = translationEntry.getValue();

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

		return result;
	}

	@Override
	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translator.getTranslationsMap();
	}

	@Override
	public Map<NerdKey, TranslationData> getUntranslatedMap() {
		return translator.getUntranslatedMap();
	}

	@Override
	public Map<NerdKey, TranslationData> getUnusedTranslationsMap() {
		return translator.getUnusedTranslationsMap();
	}

	public void init() throws DocumentException {

		final Map<NerdKey, TranslationData> translationsMap = newHashMap();
		translator = new Translator( translationsMap );

		translator.addTranslationMap( getTranslationMap( new File( TRANSLATIONS_PATH ) ) );
	}

	private Map<NerdKey, TranslationData> getTranslationMap( final File dir ) {

		final File[] farr = dir.listFiles();
		if ( farr == null ) {
			return newHashMap();
		}

		final List<File> files = Arrays.asList( farr );

		final Map<NerdKey, TranslationData> result = newHashMap();
		for ( final File file : files ) {
			if ( file.isDirectory() ) {
				result.putAll( getTranslationMap( file ) );
				continue;
			}

			try {
				result.putAll( TranslationsReader.getTranslationMap( file ) );
			} catch ( final DocumentException e ) {
				LOGGER.error( String.format( "Can not load translation from file '%s'", file.getAbsolutePath() ), e );
			}
		}

		return result;
	}

	@Override
	public void reloadTranslations() throws DocumentException {

		translator.clearUntranslatedMap();

		init();
	}

	@Override
	public Language getLanguage( final String code ) {

		final Language language = Language.getByCode( code );

		if ( language != null ) {
			return language;
		}

		return DEFAULT_LANGUAGE;
	}

	@Override
	public Language getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}

	public void setTranslator( final Translator translator ) {
		this.translator = translator;
	}
}
