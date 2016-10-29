package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.service.GenericService;
import betmen.core.service.NamedEntityGenericService;

public interface UserDao extends GenericService<User>, NamedEntityGenericService<User> {

    String CACHE_ENTRY = "totalizator.app.cache.user";
    String CACHE_QUERY = "totalizator.app.cache.users";

    User findByLogin(final String login);
}
