package betmen.web.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
public class SessionListener implements HttpSessionListener {

    private static final int SESSION_TIMEOUT_SECONDS = 3600;

    @Override
    public void sessionCreated(final HttpSessionEvent event) {
        log.debug("==== Session created ====");
        final HttpSession session = event.getSession();
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        log.debug("==== Session destroyed ====");
    }
}
