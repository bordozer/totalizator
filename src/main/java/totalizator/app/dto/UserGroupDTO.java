package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserGroupDTO {

	private int userGroupId;

	private String userGroupName;

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId( final int userGroupId ) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName( final String userGroupName ) {
		this.userGroupName = userGroupName;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", userGroupId, userGroupName );
	}
}
