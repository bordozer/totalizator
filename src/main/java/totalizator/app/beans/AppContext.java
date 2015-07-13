package totalizator.app.beans;

import totalizator.app.translator.Language;

public class AppContext {

	private Language language;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}
}
