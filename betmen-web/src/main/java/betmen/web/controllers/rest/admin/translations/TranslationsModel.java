package betmen.web.controllers.rest.admin.translations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationsModel {
    private String userName;
    private List<TranslationsDTO> untranslatedList;
}
