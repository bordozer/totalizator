package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserGroupDTO {

	private int userGroupId;

	private String userGroupName;
	private List<Integer> cupIds;

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

	public List<Integer> getCupIds() {
		return cupIds;
	}

	public void setCupIds( final List<Integer> cupIds ) {
		this.cupIds = cupIds;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", userGroupId, userGroupName );
	}
}
