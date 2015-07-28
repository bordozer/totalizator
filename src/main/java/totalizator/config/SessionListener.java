package totalizator.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	private static final int SESSION_TIMEOUT_SECONDS = 3600;

	@Override
	public void sessionCreated( final HttpSessionEvent event ) {

		System.out.println( "==== Session is created ====" );

		final HttpSession session = event.getSession();
		session.setMaxInactiveInterval( SESSION_TIMEOUT_SECONDS );
	}

	@Override
	public void sessionDestroyed( HttpSessionEvent event ) {
		System.out.println( "==== Session is destroyed ====" );
	}
}
