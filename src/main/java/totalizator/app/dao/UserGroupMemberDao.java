package totalizator.app.dao;

import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupMember;
import totalizator.app.services.GenericService;

import java.util.List;

public interface UserGroupMemberDao extends GenericService<UserGroupMember> {

	List<UserGroupMember> loadUserGroupMembers( final UserGroup userGroup );

	List<UserGroupMember> loadUserGroupsWhereUserIsMember( final User user );

	UserGroupMember load( final UserGroup userGroup, final User user );

	void delete( final UserGroup userGroup, final User user );

	void deleteAll( UserGroup userGroup );
}
