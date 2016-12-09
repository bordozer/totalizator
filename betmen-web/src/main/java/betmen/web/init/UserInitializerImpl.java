package betmen.web.init;

import betmen.core.entity.User;
import betmen.core.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component(value = "userInitializer")
public class UserInitializerImpl {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserService userService;

    private static String constructUserName(final UserData userData) {
        return String.format("%s %s %s", userData.getFirstName(), userData.getLastName(), userData.getThirdName());
    }

    public User generateAdmin() {
        return generateUser(new UserData("Hakeem", "Olajuwon"));
    }

    public User generateUser(final UserData userData) {
        final SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        final Session session = sessionFactory.openSession();

        final Transaction transaction = session.beginTransaction();

        final String login = userData.getFirstName().toLowerCase();
        final User user = new User();
        user.setLogin(login);
        user.setUsername(constructUserName(userData));
        user.setPassword(userService.encodePassword(login));
        session.persist(user);

        transaction.commit();

        return user;
    }
}
