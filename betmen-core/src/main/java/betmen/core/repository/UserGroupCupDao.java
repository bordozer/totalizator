package betmen.core.repository;

import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupCupEntity;

import java.util.List;

public interface UserGroupCupDao {

    List<UserGroupCupEntity> loadAll();

    UserGroupCupEntity load(final int id);

    UserGroupCupEntity save(UserGroupCupEntity entry);

    void delete(final int id);

    List<UserGroupCupEntity> loadCups(final UserGroupEntity userGroupEntity);

    void delete(final UserGroupCupEntity userGroupCupEntity);

    void deleteAll(final UserGroupEntity userGroupEntity);
}
