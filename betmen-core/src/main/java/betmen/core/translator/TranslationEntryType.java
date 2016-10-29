package betmen.core.translator;

public enum TranslationEntryType {

    TRANSLATED("TranslationEntryType: Translated"), NERD_TRANSLATION("TranslationEntryType: Nerd"), MISSED_LANGUAGE("TranslationEntryType: Language tag is missed in translation.xml");

    private final String description;

    private TranslationEntryType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
