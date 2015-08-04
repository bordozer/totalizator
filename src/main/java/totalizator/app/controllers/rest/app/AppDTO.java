package totalizator.app.controllers.rest.app;

import totalizator.app.dto.UserDTO;

public class AppDTO {

	private final String projectName;
	private final LanguageDTO language;
	private UserDTO currentUser;

	public AppDTO( final String projectName, final LanguageDTO language ) {
		this.projectName = projectName;
		this.language = language;
	}

	public String getProjectName() {
		return projectName;
	}

	public LanguageDTO getLanguage() {
		return language;
	}

	public UserDTO getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser( final UserDTO currentUser ) {
		this.currentUser = currentUser;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s ( %s )", projectName, currentUser, language );
	}
}
