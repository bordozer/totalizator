package betmen.web.config.servlet;

import betmen.core.model.AppContext;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorServiceImpl;
import betmen.dto.utils.Parameters;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestListener extends RequestContextListener {

    @Override
    public void requestInitialized(final ServletRequestEvent requestEvent) {
        super.requestInitialized(requestEvent);

        final HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();

        initContext(request);
    }

    private void initContext(final HttpServletRequest request) {

        final HttpSession session = request.getSession();

        final AppContext context = AppContext.read(session);
        if (context != null) {
            return;
        }

        AppContext.init(getLanguage(request), session);
    }

    private Language getLanguage(final HttpServletRequest request) {

        final Language cookie = getLanguageFromCookie(request);
        if (cookie != null) {
            return cookie;
        }

        return TranslatorServiceImpl.DEFAULT_LANGUAGE; // TODO: translatorService.getDefaultLanguage();
    }

    private Language getLanguageFromCookie(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        // cookies can be null according to documentation. return null here to prevent NPE in foreach
        if (cookies == null) {
            return null;
        }

        for (final Cookie cookie : cookies) {
            if (cookie.getName().contentEquals(Parameters.LANGUAGE_COOKIE_NAME)) {
                return Language.getByCode(cookie.getValue());
            }
        }

        return null;
    }
}
