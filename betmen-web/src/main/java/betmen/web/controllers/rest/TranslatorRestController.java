package betmen.web.controllers.rest;

import betmen.core.model.AppContext;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.TranslationDTO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/rest/translator")
public class TranslatorRestController {

    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public TranslationDTO getDefaultLogin(final TranslationDTO dto, final HttpServletRequest request) {
        final Language language = getLanguage(request.getSession());
        return new TranslationDTO(Maps.transformValues(dto.getTranslations(), nerd -> translate(nerd, language)));
    }

    private String translate(final String text, final Language language) {
        return translatorService.translate(text, language);
    }

    private Language getLanguage(final HttpSession session) {
        return AppContext.read(session).getLanguage();
    }
}
