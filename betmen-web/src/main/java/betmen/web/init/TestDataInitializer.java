package betmen.web.init;

import betmen.core.entity.User;
import betmen.core.service.LogoService;
import betmen.core.service.points.recalculation.MatchPointsTotalRecalculationService;
import betmen.web.init.initializers.AbstractDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Component
public class TestDataInitializer {

    private final List<UserData> userDatas = newArrayList();
    @Autowired
    private final List<AbstractDataInitializer> initializations = newArrayList();
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private LogoService logoService;
    @Autowired
    private MatchPointsTotalRecalculationService matchPointsTotalRecalculationService;
    @Autowired
    private UserInitializerImpl userInitializer;

    {
        userDatas.add(new UserData("Kareem", "Abdul", "Jabbar"));
        userDatas.add(new UserData("Patrick", "Aloysius", "Ewing"));
        userDatas.add(new UserData("Clyde", "Austin", "Drexler"));
        userDatas.add(new UserData("Shaquille", "Rashaun", "O'Neal"));
        userDatas.add(new UserData("Michael", "Jeffrey", "Jordan"));
        userDatas.add(new UserData("Dennis", "Keith", "Rodman"));
    }

    public void init() throws Exception {
        logoService.deleteLogosDir();
        logoService.createLogosDir();
        generateData();
    }

    private void generateData() throws IOException, DocumentException {
        final SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        final Session session = sessionFactory.openSession();

        final List<User> users = generateUsers();

        final Transaction transaction = session.beginTransaction();

        for (final AbstractDataInitializer initializer : initializations) {
            initializer.generate(users, session);
        }

        transaction.commit();

        matchPointsTotalRecalculationService.run();

        LOGGER.debug("========================================================================");
        LOGGER.debug("=                          TEST DATA CREATED                           =");
        LOGGER.debug("========================================================================");
    }

    private List<User> generateUsers() {
        List<User> res = new ArrayList<>();
        res.add(userInitializer.generateAdmin()); // should be first generated user
        res.addAll(userDatas.stream().map(userData -> userInitializer.generateUser(userData)).collect(Collectors.toList()));
        return res;
    }
}
