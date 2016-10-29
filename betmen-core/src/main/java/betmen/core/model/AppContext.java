package betmen.core.model;

import betmen.core.translator.Language;

import javax.servlet.http.HttpSession;

public class AppContext {

    private static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";
    private Language language;
    private int timeZone;

    public static AppContext init(final Language language, final HttpSession session) {

        final AppContext context = new AppContext();
        context.setLanguage(language);

        session.setAttribute(APPLICATION_CONTEXT, context);

        return context;
    }

    public static AppContext read(final HttpSession session) {
        return (AppContext) session.getAttribute(AppContext.APPLICATION_CONTEXT);
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public void setTimeZone(final int timeZone) {
        this.timeZone = timeZone;
    }

    public int getTimeZone() {
        return timeZone;
    }
}
