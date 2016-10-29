package betmen.core.translator;

import java.util.List;

public class TranslationData {

    private final String nerd;

    private final List<TranslationEntry> translations;

    private Integer usageIndex = 0;

    public TranslationData(final String nerd, final List<TranslationEntry> translations) {
        this.nerd = nerd;
        this.translations = translations;
    }

    public TranslationData(final String nerd, final List<TranslationEntry> translations, final int usageIndex) {
        this.nerd = nerd;
        this.translations = translations;
        this.usageIndex = usageIndex;
    }

    public TranslationEntry getTranslationEntry(final Language language) {
        for (final TranslationEntry translationEntry : translations) {
            if (translationEntry.getLanguage() == language) {
                return translationEntry;
            }
        }

        return new TranslationEntryMissed(nerd, language);
    }

    public String getNerd() {
        return nerd;
    }

    public List<TranslationEntry> getTranslations() {
        return translations;
    }

    public int getUsageIndex() {
        return usageIndex;
    }

    public void increaseUseIndex() {
        synchronized (usageIndex) {
            usageIndex++;
        }
    }
}
