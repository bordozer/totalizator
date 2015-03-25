package totalizator.app.controllers.rest.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PortalPageDTO {

	private int id = 0;

	private int userId;
	private String userName;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}
}
