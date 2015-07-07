package totalizator.app.dao;

import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupMember;
import totalizator.app.services.GenericService;

import java.util.List;

public interface UserGroupUserDao extends GenericService<UserGroupMember> {

	List<UserGroupMember> loadUsers( final UserGroup userGroup );
}
