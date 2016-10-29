package betmen.core.service;

import betmen.core.entity.User;

public interface UserService extends GenericService<User>, NamedEntityGenericService<User> {

    User createUser(final String login, final String name, final String password);

    User findByLogin(final String login);

    String encodePassword(final String password);

    void updateUserPassword(final User user, final String password);

    User loadAndAssertExists(int userId);
}
