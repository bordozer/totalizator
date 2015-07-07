package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupCup;

import java.util.List;

public interface UserGroupService extends GenericService<UserGroup> {

	List<UserGroup> loadAllOwned( final User user );

	List<UserGroup> loadAll( final User user );

	List<Cup> loadCups( final UserGroup userGroup );

	UserGroup save( final UserGroup userGroup, final List<Integer> cupIds );

	boolean isOwner( final UserGroup userGroup, final User user );
}
