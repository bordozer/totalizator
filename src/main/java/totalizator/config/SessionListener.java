package totalizator.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	public static final int SESSION_TIMEOUT_SECONDS = 3600;

	@Override
	public void sessionCreated( HttpSessionEvent event ) {
		System.out.println( "==== Session is created ====" );
		event.getSession().setMaxInactiveInterval( SESSION_TIMEOUT_SECONDS );
	}

	@Override
	public void sessionDestroyed( HttpSessionEvent event ) {
		System.out.println( "==== Session is destroyed ====" );
	}
}
