package totalizator.config.servlet;

import org.springframework.web.context.request.RequestContextListener;
import totalizator.app.beans.AppContext;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorServiceImpl;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestListener extends RequestContextListener {

	public static final String LANGUAGE_COOKIE_NAME = "language";

	@Override
	public void requestInitialized( final ServletRequestEvent requestEvent ) {
		super.requestInitialized( requestEvent );

		final HttpServletRequest request = ( HttpServletRequest ) requestEvent.getServletRequest();

		initContext( request );
	}

	private void initContext( final HttpServletRequest request ) {

		final HttpSession session = request.getSession();

		final AppContext context = AppContext.read( session );
		if ( context != null ) {
			return;
		}

		AppContext.init( getLanguage( request ), session );
	}

	private Language getLanguage( final HttpServletRequest request ) {

		final Language cookie = getLanguageFromCookie( request );
		if ( cookie != null ) {
			return cookie;
		}

		return TranslatorServiceImpl.DEFAULT_LANGUAGE; // TODO: translatorService.getDefaultLanguage();
	}

	private Language getLanguageFromCookie( final HttpServletRequest request ) {
		final Cookie[] cookies = request.getCookies();

		// cookies can be null according to documentation. return null here to prevent NPE in foreach
		if ( cookies == null ) {
			return null;
		}

		for ( final Cookie cookie : cookies ) {
			if ( cookie.getName().contentEquals( LANGUAGE_COOKIE_NAME ) ) {
				return Language.getByCode( cookie.getValue() );
			}
		}

		return null;
	}
}
