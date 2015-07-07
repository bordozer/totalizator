package totalizator.app.dao;

import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.GenericService;

import java.util.List;

public interface UserGroupDao extends GenericService<UserGroup> {

	String CACHE_ENTRY = "totalizator.app.cache.user-group";
	String CACHE_QUERY = "totalizator.app.cache.user-group.query";

	List<UserGroup> loadUserGroupsWhereUserIsOwner( final User user );
}
