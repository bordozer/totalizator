package betmen.core.translator;

import org.apache.commons.lang.StringUtils;

public class TranslationEntry {

    protected final String nerd;

    protected final Language language;
    protected final String value;

    public TranslationEntry(final String nerd, final Language language, final String value) {
        this.nerd = nerd;

        this.language = language;
        this.value = value;
    }

    public String getNerd() {
        return nerd;
    }

    public Language getLanguage() {
        return language;
    }

    public String getValueWithPrefixes() {
        return String.format("%s<sup>%s</sup>", value, language.getCode());
    }

    public String getValue() {
        return value;
    }

    public TranslationEntryType getTranslationEntryType() {
        return TranslationEntryType.TRANSLATED;
    }

    protected String getPrefix(final String prefix) {
        return StringUtils.isNotEmpty(prefix) ? prefix : StringUtils.EMPTY;
    }

    @Override
    public int hashCode() {
        return nerd.hashCode() * 31;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        final TranslationEntry translationEntry = (TranslationEntry) obj;
        return translationEntry.getLanguage() == language && translationEntry.getNerd().equals(nerd);
    }

    @Override
    public String toString() {
        return String.format("%s: %s '%s'", nerd, language, value);
    }
}
