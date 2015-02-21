package totalizator.app.init;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.User;

import javax.persistence.EntityManagerFactory;

@Component
public class TestDataInitializer {

	private static final Logger LOGGER = Logger.getLogger( TestDataInitializer.class );

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public void init() throws Exception {

		final SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );

		final Session session = sessionFactory.openSession();

		final Transaction transaction = session.beginTransaction();

		/*for ( int i = 0; i < 10; i++ ) {
			final User user = new User( String.format( "login_%d", i ), String.format( "name_%d", i ), "$2a$10$x9vXeDsSC2109FZfIJz.pOZ4dJ056xBpbesuMJg3jZ.ThQkV119tS" );
			session.persist( user );
		}*/

		final User user1 = new User( "kareem", "Kareem Abdul Jabbar", "$2a$10$11.wSzimtc3ALjbjokULr.LLJb4A/iDh3P6rw0..nI9JcpTpwN6M6" );
		session.persist( user1 );

		final User user2 = new User( "hakeem", "Hakeem Olajuwon", "$2a$10$VNleCLXwo0TNYCcb4m1rWOjx2SU1qs6hyav04Ip8Zq0eZu3/Al1aS" );
		session.persist( user2 );

		final User user3 = new User( "patrick", "Patrick Aloysius Ewing", "$2a$10$daF6I2MzfIcmGg/pWNmc4ep8OLkjbDDRxRhnKhMpbTOyLNacpcMVO" );
		session.persist( user3 );

		final Category category1 = new Category( "Basketball" );
		session.persist( category1 );

		final Category category2 = new Category( "Football" );
		session.persist( category2 );

		transaction.commit();

		LOGGER.debug( "========================================================================" );
		LOGGER.debug( "=                          TEST DATA IS CREATED                        =" );
		LOGGER.debug( "========================================================================" );
	}
}
