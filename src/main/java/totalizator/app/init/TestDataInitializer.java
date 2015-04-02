package totalizator.app.init;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.init.initializers.AbstractDataInitializer;
import totalizator.app.init.initializers.NBA;
import totalizator.app.init.initializers.NCAA;
import totalizator.app.init.initializers.UEFA;
import totalizator.app.models.User;
import totalizator.app.services.TeamLogoService;
import totalizator.app.services.UserService;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class TestDataInitializer {

	private static final Logger LOGGER = Logger.getLogger( TestDataInitializer.class );

	@Autowired
	private UserService userService;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private TeamLogoService teamLogoService;

	private final List<AbstractDataInitializer> initializers = newArrayList();

	private final List<UserData> userDatas = newArrayList();

	{
		userDatas.add( new UserData( "Kareem", "Abdul", "Jabbar" ) );
		userDatas.add( new UserData( "Hakeem", "Olajuwon" ) );
		userDatas.add( new UserData( "Patrick", "Aloysius", "Ewing" ) );
		userDatas.add( new UserData( "Clyde", "Austin", "Drexler" ) );
		userDatas.add( new UserData( "Shaquille", "Rashaun", "O'Neal" ) );
		userDatas.add( new UserData( "Michael", "Jeffrey", "Jordan" ) );
		userDatas.add( new UserData( "Dennis", "Keith", "Rodman" ) );
	}

	@Autowired
	private NBA nba;

	@Autowired
	private NCAA ncaa;

	@Autowired
	private UEFA uefa;

	public void init() throws Exception {

		teamLogoService.deleteLogosDir();
		teamLogoService.createLogosDir();

		final SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );

		final Session session = sessionFactory.openSession();

		final Transaction transaction = session.beginTransaction();

		final List<User> users = generateUsers( session );

		initializers.add( nba );
		initializers.add( ncaa );
		initializers.add( uefa );
		for ( final AbstractDataInitializer initializer : initializers ) {
			initializer.generate( users, session );
		}

		transaction.commit();

		LOGGER.debug( "========================================================================" );
		LOGGER.debug( "=                          TEST DATA CREATED                           =" );
		LOGGER.debug( "========================================================================" );
	}

	private List<User> generateUsers( final Session session ) {

		final List<User> result = newArrayList();

		for ( final UserData userData : userDatas ) {

			final String login = userData.firstName.toLowerCase();
			final User user = new User( login
					, String.format( "%s %s %s", userData.firstName, userData.lastName, userData.thirdName )
					, userService.encodePassword( login )
			);
			session.persist( user );

			result.add( user );
		}


		return result;
	}

	private class UserData {

		UserData( final String firstName, final String lastName ) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.thirdName = "";
		}

		UserData( final String firstName, final String lastName, final String thirdName ) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.thirdName = thirdName;
		}

		final String firstName;
		final String lastName;
		final String thirdName;
	}
}
