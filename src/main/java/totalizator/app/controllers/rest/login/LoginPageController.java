package totalizator.app.controllers.rest.login;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "login" )
public class LoginPageController {

	private final Logger LOGGER = Logger.getLogger( LoginPageController.class );

	/*@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LoginDTO showLoginForm( final @RequestBody LoginDTO loginDTO ) {
		final Language language = Language.RU;

		final Map<String, String> translation = newHashMap();
		translation.put( "loginLabel", translatorService.translate( "Login page: Login", language ) );
		translation.put( "passwordLabel", translatorService.translate( "Login page: Password", language ) );
		translation.put( "createNewUserLabel", translatorService.translate( "Login page: Create new user", language ) );
//		translation.put( "", translatorService.translate( "", language ) );
//		translation.put( "", translatorService.translate( "", language ) );

		loginDTO.setTranslations( translation );

		return loginDTO;
	}*/

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LoginDTO doLogin( final @RequestBody LoginDTO loginDTO ) {
		return loginDTO;
	}
}
