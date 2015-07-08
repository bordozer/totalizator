package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserGroupDTO {

	private int userGroupId;

	private String userGroupName;
	private List<CupDTO> userGroupCups;
	private UserDTO userGroupOwner;

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

	public List<CupDTO> getUserGroupCups() {
		return userGroupCups;
	}

	public void setUserGroupCups( final List<CupDTO> userGroupCups ) {
		this.userGroupCups = userGroupCups;
	}

	public UserDTO getUserGroupOwner() {
		return userGroupOwner;
	}

	public void setUserGroupOwner( final UserDTO userGroupOwner ) {
		this.userGroupOwner = userGroupOwner;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", userGroupId, userGroupName );
	}
}
