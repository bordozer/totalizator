package betmen.web.controllers.rest.admin.translations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationEntryDTO {

    private String language;
    private String nerd;

    public TranslationEntryDTO() {
    }

    public TranslationEntryDTO(final String language, final String nerd) {
        this.language = language;
        this.nerd = nerd;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getNerd() {
        return nerd;
    }

    public void setNerd(final String nerd) {
        this.nerd = nerd;
    }
}
