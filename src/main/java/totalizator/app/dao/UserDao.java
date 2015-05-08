package totalizator.app.dao;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

public interface UserDao extends GenericService<User>, NamedEntityGenericService<User> {

	String CACHE_ENTRY = "totalizator.app.cache.user";
	String CACHE_QUERY = "totalizator.app.cache.users";

	@Cacheable( value = UserDao.CACHE_QUERY )
	User findByLogin( String login );
}
