package betmen.core.repository;

import betmen.core.entity.UserGroupEntity;

import java.util.List;

public interface UserGroupDao {

    String CACHE_ENTRY = "totalizator.app.cache.user-group";
    String CACHE_QUERY = "totalizator.app.cache.user-group.query";

    List<UserGroupEntity> loadAll();

    UserGroupEntity load(final int id);

    UserGroupEntity save(UserGroupEntity entry);

    void delete(final int id);

    List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final int userId, final int cupId);
}
