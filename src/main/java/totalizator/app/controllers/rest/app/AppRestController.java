package totalizator.app.controllers.rest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.beans.AppContext;
import totalizator.app.services.DTOService;
import totalizator.app.services.SystemVarsService;
import totalizator.app.services.UserService;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

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

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public AppDTO applicationData( final ClientData clientData, final Principal principal, final HttpServletRequest request ) {

		final String projectName = systemVarsService.getProjectName();

		final AppContext appContext = AppContext.read( request.getSession() );
		appContext.setTimeZone( clientData.getTimezone() );

		final Language lang = appContext.getLanguage();
		final LanguageDTO language = new LanguageDTO( translatorService.translate( lang.getName(), lang ), lang.getCountry() );

		final AppDTO dto = new AppDTO( projectName, language );

		final LocalDateTime now = dateTimeService.getNow();
		dto.setServerNow( now );

		if ( principal != null ) {
			dto.setCurrentUser( dtoService.transformUser( userService.findByLogin( principal.getName() ) ) );
		}

		return dto;
	}
}
