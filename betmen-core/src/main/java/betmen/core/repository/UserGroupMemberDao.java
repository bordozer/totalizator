package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.entity.UserGroupMember;
import betmen.core.service.GenericService;

import java.util.List;

public interface UserGroupMemberDao extends GenericService<UserGroupMember> {

    List<UserGroupMember> loadUserGroupMembers(final UserGroup userGroup);

    List<UserGroupMember> loadUserGroupsWhereUserIsMember(final int userId);

    List<UserGroupMember> loadUserGroupsWhereUserIsMember(final int userId, final int cupId);

    UserGroupMember load(final UserGroup userGroup, final User user);

    void delete(final UserGroup userGroup, final User user);

    void deleteAll(UserGroup userGroup);
}
