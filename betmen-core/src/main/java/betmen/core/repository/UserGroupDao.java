package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.service.GenericService;

import java.util.List;

public interface UserGroupDao extends GenericService<UserGroup> {

    String CACHE_ENTRY = "totalizator.app.cache.user-group";
    String CACHE_QUERY = "totalizator.app.cache.user-group.query";

    List<UserGroup> loadUserGroupsWhereUserIsOwner(final User user);
}
