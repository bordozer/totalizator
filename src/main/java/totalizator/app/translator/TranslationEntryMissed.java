package totalizator.app.translator;

public class TranslationEntryMissed extends TranslationEntry {

	public TranslationEntryMissed( final String nerd, final Language language ) {
		super( nerd, language, nerd );
	}

	@Override
	public TranslationEntryType getTranslationEntryType() {
		return TranslationEntryType.MISSED_LANGUAGE;
	}

	@Override
	public String getValueWithPrefixes() {
		return String.format( "%s<sup>!</sup>", super.getValueWithPrefixes() );
	}
}
