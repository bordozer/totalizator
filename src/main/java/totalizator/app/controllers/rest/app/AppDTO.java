package totalizator.app.controllers.rest.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class AppDTO {

	private final String projectName;
	private final LanguageDTO language;

	private LocalDateTime timeNow;
	private String timeNowFormatted;

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

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getTimeNow() {
		return timeNow;
	}

	public void setTimeNow( LocalDateTime timeNow ) {
		this.timeNow = timeNow;
	}

	public String getTimeNowFormatted() {
		return timeNowFormatted;
	}

	public void setTimeNowFormatted( String timeNowFormatted ) {
		this.timeNowFormatted = timeNowFormatted;
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
