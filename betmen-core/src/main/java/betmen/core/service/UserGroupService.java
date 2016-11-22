package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;

import java.util.List;

public interface UserGroupService {

    List<UserGroupEntity> loadAll();

    UserGroupEntity load(final int id);

    UserGroupEntity save(UserGroupEntity entry);

    void delete(final int id);

    List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final User user);

    List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final User user, final int cupId);

    List<UserGroupEntity> loadUserGroupsWhereUserIsMember(final User user);

    List<UserGroupEntity> loadUserGroupsWhereUserIsMember(final User user, final int cupId);

    List<User> loadUserGroupMembers(final UserGroupEntity userGroupEntity);

    List<Cup> loadCups(final UserGroupEntity userGroupEntity);

    boolean isUserOwnerOfGroup(final UserGroupEntity userGroupEntity, final User user);

    boolean isUserMemberOfGroup(final UserGroupEntity userGroupEntity, final User user);

    void addMember(final UserGroupEntity userGroupEntity, final User user);

    void removeMember(final UserGroupEntity userGroupEntity, final User user);
}
