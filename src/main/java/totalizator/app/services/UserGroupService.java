package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import java.util.List;

public interface UserGroupService extends GenericService<UserGroup> {

	List<UserGroup> loadAllWhereIsOwner( final User user );

	List<UserGroup> loadAllWhereIsMember( final User user );

	List<Cup> loadCups( final UserGroup userGroup );

	List<User> loadGroupMembers( final UserGroup userGroup );

	UserGroup save( final UserGroup userGroup, final List<Integer> cupIds );

	boolean isOwner( final UserGroup userGroup, final User user );
}
