package totalizator.app.init;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
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



		final User user1 = new User( "kareem", "Kareem Abdul Jabbar", "$2a$10$11.wSzimtc3ALjbjokULr.LLJb4A/iDh3P6rw0..nI9JcpTpwN6M6" );
		session.persist( user1 );

		final User user2 = new User( "hakeem", "Hakeem Olajuwon", "$2a$10$VNleCLXwo0TNYCcb4m1rWOjx2SU1qs6hyav04Ip8Zq0eZu3/Al1aS" );
		session.persist( user2 );

		final User user3 = new User( "patrick", "Patrick Aloysius Ewing", "$2a$10$daF6I2MzfIcmGg/pWNmc4ep8OLkjbDDRxRhnKhMpbTOyLNacpcMVO" );
		session.persist( user3 );



		final Category category1 = new Category( "NBA" );
		session.persist( category1 );

		final Category category2 = new Category( "NCAA" );
		session.persist( category2 );

		final Category category3 = new Category( "Football" );
		session.persist( category3 );



		/*final Cup cup1 = new Cup( "NBA 2015 - regular" );
		session.persist( cup1 );

		final Cup cup2 = new Cup( "NBA 2015 - playoff" );
		session.persist( cup2 );

		final Cup cup3 = new Cup( "NCAA 2015" );
		session.persist( cup3 );*/



		transaction.commit();

		LOGGER.debug( "========================================================================" );
		LOGGER.debug( "=                          TEST DATA IS CREATED                        =" );
		LOGGER.debug( "========================================================================" );
	}
}
