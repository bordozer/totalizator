package totalizator.app.services;

import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import java.util.List;

public interface UserGroupService extends GenericService<UserGroup> {

	List<UserGroup> loadAllOwned( final User user );

	List<UserGroup> loadAll( final User user );

	boolean isOwner( final UserGroup userGroup, final User user );
}
