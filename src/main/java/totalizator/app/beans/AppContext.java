package totalizator.app.beans;

import totalizator.app.translator.Language;

import javax.servlet.http.HttpSession;

public class AppContext {

	private static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";
	private Language language;

	public static AppContext init( final Language language, final HttpSession session ) {

		if ( session.getAttribute( APPLICATION_CONTEXT ) != null ) {
			return ( AppContext ) session.getAttribute( APPLICATION_CONTEXT );
		}

		synchronized ( AppContext.class ) {

			if ( session.getAttribute( APPLICATION_CONTEXT ) != null ) {
				return ( AppContext ) session.getAttribute( APPLICATION_CONTEXT );
			}

			final AppContext context = new AppContext();
			context.setLanguage( language );

			session.setAttribute( APPLICATION_CONTEXT, context );

			return context;
		}
	}

	public static AppContext read( final HttpSession session ) {
		return ( AppContext ) session.getAttribute( AppContext.APPLICATION_CONTEXT );
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}
}
