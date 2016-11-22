package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupMemberEntity;

import java.util.List;

public interface UserGroupMemberDao {

    List<UserGroupMemberEntity> loadAll();

    UserGroupMemberEntity load(final int id);

    UserGroupMemberEntity save(UserGroupMemberEntity entry);

    void delete(final int id);

    List<UserGroupMemberEntity> loadUserGroupMembers(final UserGroupEntity userGroupEntity);

    List<UserGroupMemberEntity> loadUserGroupsWhereUserIsMember(final int userId);

    List<UserGroupMemberEntity> loadUserGroupsWhereUserIsMember(final int userId, final int cupId);

    UserGroupMemberEntity load(final UserGroupEntity userGroupEntity, final User user);

    void delete(final UserGroupEntity userGroupEntity, final User user);

    void deleteAll(UserGroupEntity userGroupEntity);
}
