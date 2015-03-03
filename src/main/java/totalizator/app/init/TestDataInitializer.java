package totalizator.app.init;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import javax.persistence.EntityManagerFactory;

@Component
public class TestDataInitializer {

	private static final Logger LOGGER = Logger.getLogger( TestDataInitializer.class );

	private static final String CATEGORY_NBA = "NBA";
	private static final String CATEGORY_NCAA = "NCAA";
	private static final String CATEGORY_UEFA = "UEFA";

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



		final Category nba = new Category( CATEGORY_NBA );
		session.persist( nba );

		final Category ncaa = new Category( CATEGORY_NCAA );
		session.persist( ncaa );

		final Category uefa = new Category( CATEGORY_UEFA );
		session.persist( uefa );




		final Cup nba2015Regular = new Cup( "2015 - regular", nba );
		session.persist( nba2015Regular );

		final Cup nba2015PlayOff = new Cup( "2015 - playoff", nba );
		session.persist( nba2015PlayOff );

		final Cup ncaa2015 = new Cup( "2015", ncaa );
		session.persist( ncaa2015 );

		final Cup uefa2016Euro = new Cup( "Euro 2016", uefa );
		session.persist( uefa2016Euro );

		final Cup uefa2016WorldCup = new Cup( "World cup 2018", uefa );
		session.persist( uefa2016WorldCup );



		final Team oklahoma = new Team( "Oklahoma City Thunder", nba.getId() );
		session.persist( oklahoma );

		final Team chicago = new Team( "Chicago Bulls", nba.getId() );
		session.persist( chicago );

		final Team houston = new Team( "Houston Rockets", nba.getId() );
		session.persist( houston );

		final Team newYork = new Team( "New York Knicks", nba.getId() );
		session.persist( newYork );

		final Team clippers = new Team( "Los Angeles Clippers", nba.getId() );
		session.persist( clippers );

		final Team duke = new Team( "Duke", ncaa.getId() );
		session.persist( duke );

		final Team syracuse = new Team( "Syracuse", ncaa.getId() );
		session.persist( syracuse );

		final Team virginia = new Team( "Virginia", ncaa.getId() );
		session.persist( virginia );

		final Team mexico = new Team( "New Mexico", ncaa.getId() );
		session.persist( mexico );



		final Team spain = new Team( "Spain", uefa.getId() );
		session.persist( spain );

		final Team ukraine = new Team( "Ukraine", uefa.getId() );
		session.persist( ukraine );

		final Team switzerland = new Team( "Switzerland", uefa.getId() );
		session.persist( switzerland );

		final Team netherlands = new Team( "Netherlands", uefa.getId() );
		session.persist( netherlands );




		transaction.commit();

		LOGGER.debug( "========================================================================" );
		LOGGER.debug( "=                          TEST DATA IS CREATED                        =" );
		LOGGER.debug( "========================================================================" );
	}
}
