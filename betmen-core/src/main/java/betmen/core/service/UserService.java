package betmen.core.service;

import betmen.core.entity.User;

import java.util.List;

public interface UserService {

    List<User> loadAll();

    User load(final int id);

    User save(User entry);

    void delete(final int id);

    User findByName(final String name);

    User createUser(final String login, final String name, final String password);

    User findByLogin(final String login);

    String encodePassword(final String password);

    void updateUserPassword(final User user, final String password);

    User loadAndAssertExists(int userId);
}
