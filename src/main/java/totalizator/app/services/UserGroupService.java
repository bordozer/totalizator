package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import java.util.List;

public interface UserGroupService extends GenericService<UserGroup> {

	List<UserGroup> loadUserGroupsWhereUserIsOwner( final User user );

	List<UserGroup> loadUserGroupsWhereUserIsMember( final User user );

	List<User> loadUserGroupMembers( final UserGroup userGroup );

	List<Cup> loadCups( final UserGroup userGroup );

	UserGroup save( final UserGroup userGroup, final List<Integer> cupIds );

	boolean isUserOwnerOfGroup( final UserGroup userGroup, final User user );

	void addMember( final UserGroup userGroup, final User user );

	void removeMember( final UserGroup userGroup, final User user );
}
