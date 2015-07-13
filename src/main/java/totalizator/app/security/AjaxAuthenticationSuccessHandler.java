package totalizator.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import totalizator.app.beans.AppContext;
import totalizator.app.translator.Language;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	public static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";

	private AuthenticationSuccessHandler defaultHandler;

	public AjaxAuthenticationSuccessHandler() {
		this.defaultHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	}

	@Override
	public void onAuthenticationSuccess( final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication ) throws IOException, ServletException {

		if ( "true".equals( request.getHeader( "X-Login-Ajax-call" ) ) ) {

			final Language language = Language.getByCode( request.getParameter( "language" ) );

			final AppContext context = new AppContext();
			context.setLanguage( language );

			final HttpSession session = request.getSession();
			session.setAttribute( APPLICATION_CONTEXT, context );

			response.getWriter().print( "ok" );
			response.getWriter().flush();
		} else {
			defaultHandler.onAuthenticationSuccess( request, response, authentication );
		}
	}
}
