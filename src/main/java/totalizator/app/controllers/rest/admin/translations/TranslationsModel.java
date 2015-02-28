package totalizator.app.controllers.rest.admin.translations;

import java.util.List;

public class TranslationsModel {

	private String userName;

	private List<TranslationsDTO> untranslatedList;

	public List<TranslationsDTO> getUntranslatedList() {
		return untranslatedList;
	}

	public void setUntranslatedList( final List<TranslationsDTO> untranslatedList ) {
		this.untranslatedList = untranslatedList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}
}
