package totalizator.app.dao;

import totalizator.app.models.User;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

public interface UserDao extends GenericService<User>, NamedEntityGenericService<User> {

	String CACHE_ENTRY = "totalizator.app.cache.user";
	String CACHE_QUERY = "totalizator.app.cache.users";

	User findByLogin( String login );
}
