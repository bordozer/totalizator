package betmen.core.repository;

import betmen.core.entity.UserGroup;
import betmen.core.entity.UserGroupCup;
import betmen.core.service.GenericService;

import java.util.List;

public interface UserGroupCupDao extends GenericService<UserGroupCup> {

    List<UserGroupCup> loadCups(final UserGroup userGroup);

    void delete(final UserGroupCup userGroupCup);

    void deleteAll(final UserGroup userGroup);
}
