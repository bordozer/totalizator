package betmen.core.service;

import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.UserDao;
import betmen.core.entity.User;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userRepository;

    @Override
    @Transactional
    public User createUser(final String login, final String name, final String password) {
        // TODO: validation
        final User user = new User(login, name, encodePassword(password));
        LOGGER.debug(String.format("New user: %s, ( name: %s, password: %s )", login, name, password));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> loadAll() {
        return Lists.newArrayList(userRepository.loadAll());
    }

    @Override
    @Transactional
    public User save(final User entry) {
        return userRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public User load(final int id) {
        return userRepository.load(id);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        userRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByName(final String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public String encodePassword(final String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    @Transactional
    public void updateUserPassword(final User user, final String password) {
        user.setPassword(encodePassword(password));
        userRepository.save(user);
    }

    @Override
    public User loadAndAssertExists(final int userId) {
        User user = load(userId);
        if (user == null) {
            throw new UnprocessableEntityException(String.format("User does not exist: %d", userId));
        }
        return user;
    }
}
