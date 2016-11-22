package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.service.NamedEntityGenericService;

import java.util.List;

public interface UserDao extends NamedEntityGenericService<User> {

    String CACHE_ENTRY = "totalizator.app.cache.user";
    String CACHE_QUERY = "totalizator.app.cache.users";

    List<User> loadAll();

    User load(final int id);

    User save(User entry);

    void delete(final int id);

    User findByLogin(final String login);
}
