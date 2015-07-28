package totalizator.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import totalizator.app.beans.AppContext;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;
import totalizator.config.root.SecurityConfig;
import totalizator.config.servlet.RequestListener;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private AuthenticationSuccessHandler defaultHandler;

	@Autowired
	private TranslatorService translatorService;

	public AjaxAuthenticationSuccessHandler() {
		final SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		savedRequestAwareSuccessHandler.setTargetUrlParameter( SecurityConfig.PORTAL_PAGE_URL );
		this.defaultHandler = savedRequestAwareSuccessHandler;
	}

	@Override
	public void onAuthenticationSuccess( final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication ) throws IOException, ServletException {

		if ( "true".equals( request.getHeader( "X-Login-Ajax-call" ) ) ) {

			final Language language = getLanguage( request );

			setLanguageCookie( response, language );

			AppContext.init( language, request.getSession() );

			response.getWriter().print( "ok" );
			response.getWriter().flush();
		} else {
			defaultHandler.onAuthenticationSuccess( request, response, authentication );
		}
	}

	private Language getLanguage( final HttpServletRequest request ) {
		return translatorService.getLanguage( request.getParameter( "language" ) );
	}

	private static void setLanguageCookie( final HttpServletResponse response, final Language language ) {
		final Cookie languageCookie = new Cookie( RequestListener.LANGUAGE_COOKIE_NAME, language.getCode() );
		languageCookie.setMaxAge( Integer.MAX_VALUE );
		response.addCookie( languageCookie );
	}
}
