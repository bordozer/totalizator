package totalizator.app.init;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.User;

import javax.persistence.EntityManagerFactory;

@Component
public class TestDataInitializer {

	private static final Logger LOGGER = Logger.getLogger( TestDataInitializer.class );

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public void init() throws Exception {

		LOGGER.debug( "========================== init ===========================" );

		final SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );

		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.beginTransaction();

		/*for ( int i = 0; i < 10; i++ ) {
			final User user = new User( String.format( "login_%d", i ), String.format( "name_%d", i ), "$2a$10$x9vXeDsSC2109FZfIJz.pOZ4dJ056xBpbesuMJg3jZ.ThQkV119tS" );
			session.persist( user );
		}*/

		final User user = new User( "login_1", "name_1", "$2a$10$x9vXeDsSC2109FZfIJz.pOZ4dJ056xBpbesuMJg3jZ.ThQkV119tS" );
		session.persist( user );

		transaction.commit();

		LOGGER.debug( "=============================================================" );
		LOGGER.debug( "=                         USER CREATED                      =" );
		LOGGER.debug( "=============================================================" );
	}
}
