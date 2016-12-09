package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.model.AppContext;
import betmen.core.model.ErrorCodes;
import betmen.core.service.SecurityService;
import betmen.core.service.SystemVarsService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.AppDTO;
import betmen.dto.dto.ClientDataDTO;
import betmen.dto.dto.LanguageDTO;
import betmen.dto.dto.UserDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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
    private SecurityService securityService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public AppDTO applicationData(final ClientDataDTO clientData, final Principal principal, final HttpServletRequest request) {
        final AppContext appContext = AppContext.read(request.getSession());
        appContext.setTimeZone(clientData.getTimezone());
        final AppDTO dto = new AppDTO(systemVarsService.getProjectName(), buildLanguageDTO(appContext));
        dto.setServerNow(dateTimeService.getNow());
        if (principal != null) {
            dto.setCurrentUser(dtoService.transformUser(getCurrentUser(principal)));
        }
        return dto;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/who-am-i/")
    public UserDTO whoAmI(final Principal principal) {
        if (principal == null) {
            return new UserDTO(0, "Anonymous");
        }
        final User currentUser = getCurrentUser(principal);
        Assert.assertNotNull(currentUser, ErrorCodes.USER_NOT_FOUND);
        return dtoService.transformUser(currentUser);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authenticated/")
    public boolean authenticated(final Principal principal) {
        return principal != null && whoAmI(principal) != null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/who-am-i/is-admin/")
    public boolean isAdmin(final Principal principal) {
        if (principal == null) {
            return false;
        }
        final User currentUser = getCurrentUser(principal);
        return currentUser != null && securityService.isAdmin(currentUser.getId());
    }

    private LanguageDTO buildLanguageDTO(final AppContext appContext) {
        final Language lang = appContext.getLanguage();
        return new LanguageDTO(translatorService.translate(lang.getName(), lang), lang.getCountry());
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
