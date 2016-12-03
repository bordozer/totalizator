package betmen.web.controllers.rest.app;

import betmen.core.entity.User;
import betmen.core.model.AppContext;
import betmen.core.model.ErrorCodes;
import betmen.core.service.SystemVarsService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.UserDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/rest/app")
public class AppRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private SystemVarsService systemVarsService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public AppDTO applicationData(final ClientData clientData, final Principal principal, final HttpServletRequest request) {
        final String projectName = systemVarsService.getProjectName();
        final AppContext appContext = AppContext.read(request.getSession());
        appContext.setTimeZone(clientData.getTimezone());
        final Language lang = appContext.getLanguage();
        final LanguageDTO language = new LanguageDTO(translatorService.translate(lang.getName(), lang), lang.getCountry());
        final AppDTO dto = new AppDTO(projectName, language);
        final LocalDateTime now = dateTimeService.getNow();
        dto.setServerNow(now);
        if (principal != null) {
            dto.setCurrentUser(dtoService.transformUser(userService.findByLogin(principal.getName())));
        }
        return dto;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/who-am-i/")
    public UserDTO whoAmI(final Principal principal) {
        if (principal == null) {
            return new UserDTO(0, "Anonymous");
        }
        final User currentUser = userService.findByLogin(principal.getName());
        Assert.assertNotNull(currentUser, ErrorCodes.USER_NOT_FOUND);
        return dtoService.transformUser(currentUser);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authenticated/")
    public boolean authenticated(final Principal principal) {
        return principal != null && whoAmI(principal) != null;
    }
}
