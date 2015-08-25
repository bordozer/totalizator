package totalizator.app.controllers.rest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/app/")
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

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public AppDTO userBets( final Principal principal, final HttpServletRequest request ) {

		final String projectName = systemVarsService.getProjectName();

		final Language lang = AppContext.read( request.getSession() ).getLanguage();
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
