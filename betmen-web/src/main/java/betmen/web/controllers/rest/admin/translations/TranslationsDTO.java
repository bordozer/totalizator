package betmen.web.controllers.rest.admin.translations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TranslationsDTO {
    private String nerd;
    private List<TranslationEntryDTO> translationEntries;
}
