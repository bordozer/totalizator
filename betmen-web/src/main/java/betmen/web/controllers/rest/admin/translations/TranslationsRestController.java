package betmen.web.controllers.rest.admin.translations;

import betmen.core.service.UserService;
import betmen.core.translator.NerdKey;
import betmen.core.translator.TranslationEntry;
import betmen.core.translator.TranslatorService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin/rest/translations")
public class TranslationsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private TranslatorService translatorService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/untranslated/", produces = APPLICATION_JSON_VALUE)
    public TranslationsModel untranslated(final Principal principal) {

        final List<TranslationsDTO> untranslatedList = newArrayList();
        for (final NerdKey nerdKey : translatorService.getUntranslatedMap().keySet()) {

            final TranslationsDTO translationsDTO = new TranslationsDTO();
            translationsDTO.setNerd(nerdKey.getNerd());

            final List<TranslationEntryDTO> untranslated = newArrayList();
            for (final TranslationEntry translationEntry : translatorService.getUntranslatedMap().get(nerdKey).getTranslations()) {
                untranslated.add(new TranslationEntryDTO(translationEntry.getLanguage().getName(), translationEntry.getNerd()));
            }
            translationsDTO.setTranslationEntries(untranslated);

            untranslatedList.add(translationsDTO);
        }

        final TranslationsModel model = new TranslationsModel();
        model.setUserName(userService.findByLogin(principal.getName()).getUsername());
        model.setUntranslatedList(untranslatedList);

        return model;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/reload/")
    public void reloadTranslations() throws DocumentException {
        translatorService.reloadTranslations();
    }
}
