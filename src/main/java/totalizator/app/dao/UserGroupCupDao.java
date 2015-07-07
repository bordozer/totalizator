package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupCup;
import totalizator.app.services.GenericService;

import java.util.List;

public interface UserGroupCupDao extends GenericService<UserGroupCup> {

	List<UserGroupCup> loadAll( final UserGroup userGroup );

	void delete( final UserGroupCup userGroupCup );

	void deleteAll( final UserGroup userGroup );
}
