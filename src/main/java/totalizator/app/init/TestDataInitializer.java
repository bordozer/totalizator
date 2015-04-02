package totalizator.app.init;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.*;
import totalizator.app.services.*;
import totalizator.app.services.utils.DateTimeService;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class TestDataInitializer {

	private static final Logger LOGGER = Logger.getLogger( TestDataInitializer.class );

	private static final String CATEGORY_NBA = "NBA";
	private static final String CATEGORY_NCAA = "NCAA";
	private static final String CATEGORY_UEFA = "UEFA";

	@Autowired
	private UserService userService;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private TeamImportService teamImportService;

	@Autowired
	private TeamLogoService teamLogoService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

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

	public void init() throws Exception {

		final SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );

		final Session session = sessionFactory.openSession();

		final Transaction transaction = session.beginTransaction();

		for ( final UserData userData : userDatas ) {
			final String login = userData.firstName.toLowerCase();
			final User user1 = new User( login
					, String.format( "%s %s %s", userData.firstName, userData.lastName, userData.thirdName )
					, userService.encodePassword( login )
			);
			session.persist( user1 );
		}

		final Category nba = new Category( CATEGORY_NBA );
		session.persist( nba );

		final Category ncaa = new Category( CATEGORY_NCAA );
		session.persist( ncaa );

		final Category uefa = new Category( CATEGORY_UEFA );
		session.persist( uefa );


		final Cup nba2015Regular = new Cup( "2015 - regular", nba );
		nba2015Regular.setShowOnPortalPage( true );
		session.persist( nba2015Regular );

		final Cup nba2015PlayOff = new Cup( "2015 - playoff", nba );
		session.persist( nba2015PlayOff );

		final Cup ncaa2015 = new Cup( "2015", ncaa );
		session.persist( ncaa2015 );

		final Cup uefa2016Euro = new Cup( "Euro 2016", uefa );
		session.persist( uefa2016Euro );

		final Cup uefa2018WorldCup = new Cup( "World cup 2018", uefa );
		session.persist( uefa2018WorldCup );

		teamLogoService.deleteLogosDir();
		teamLogoService.createLogosDir();

		createNBATeams( session, nba );

		createNCAATeams( session, ncaa );

		createUEFATeams( session, uefa );



		transaction.commit();



		final Transaction transaction1 = session.beginTransaction();

		final MatchDataGenerationStrategy pastMatchesStrategy = new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			int generateScore() {
				return getRandomInt( 80, 115 );
			}

			@Override
			boolean isFinished() {
				return true;
			}
		};
		generateMatches( nba2015Regular, 100, session, pastMatchesStrategy );
		generateMatches( nba2015PlayOff, 25, session, pastMatchesStrategy );
		generateMatches( ncaa2015, 5, session, pastMatchesStrategy );
		generateMatches( uefa2016Euro, 20, session, pastMatchesStrategy );
		generateMatches( uefa2018WorldCup, 5, session, pastMatchesStrategy );



		transaction1.commit();



		final Transaction transaction2 = session.beginTransaction();

		generateBets( nba2015Regular, pastMatchesStrategy );
		generateBets( nba2015PlayOff, pastMatchesStrategy );
		generateBets( ncaa2015, pastMatchesStrategy );
		generateBets( uefa2016Euro, pastMatchesStrategy );
		generateBets( uefa2018WorldCup, pastMatchesStrategy );

		final MatchDataGenerationStrategy futureMatchesStrategy = new MatchDataGenerationStrategy() {
			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 168 ) );
			}

			@Override
			int generateScore() {
				return 0;
			}

			@Override
			boolean isFinished() {
				return false;
			}
		};
		generateMatches( nba2015Regular, 10, session, futureMatchesStrategy );

		transaction2.commit();

		LOGGER.debug( "========================================================================" );
		LOGGER.debug( "=                          TEST DATA IS CREATED                        =" );
		LOGGER.debug( "========================================================================" );
	}

	private void generateBets( final Cup cup, final MatchDataGenerationStrategy strategy ) {

		final List<User> users = userService.loadAll();

		final List<Match> cupMatches = matchService.loadAll( cup );

		for ( final User user : users ) {

			final int betsCountGenerateTo = getRandomInt( 0, cupMatches.size() - 1 );

			if ( betsCountGenerateTo == 0 ) {
				continue;
			}

			final List<Match> matches = newArrayList( cupMatches );
			final Iterator<Match> iterator = matches.iterator();

			int i = 0;
			while ( iterator.hasNext() ) {
				final Match match = iterator.next();

				final MatchBet bet = new MatchBet();
				bet.setMatch( cupMatches.get( getRandomInt( 0, matches.size() - 1 ) ) );
				bet.setUser( user );
				bet.setBetScore1( strategy.generateScore() );
				bet.setBetScore2( strategy.generateScore() );
				bet.setBetTime( dateTimeService.offset( match.getBeginningTime(), Calendar.HOUR, getRandomInt( 1, 12 ) ) );

				matchBetsService.save( bet );

				i++;

				if ( i >= betsCountGenerateTo ) {
					break;
				}
			}
		}
	}

	private void createNBATeams( final Session session, final Category category ) throws DocumentException, IOException {
		final List<TeamData> teams = teamImportService.importNBA( category );
		createTeams( session, teams );
	}

	private void createNCAATeams( final Session session, final Category category ) throws DocumentException, IOException {
		final List<TeamData> teams = teamImportService.importNCAA( category );
		createTeams( session, teams );
	}

	private void createUEFATeams( final Session session, final Category category ) throws DocumentException, IOException {
		final List<TeamData> teams = teamImportService.importUEFA( category );
		createTeams( session, teams );
	}

	private void createTeams( final Session session, final List<TeamData> teams ) throws IOException {
		for ( final TeamData teamData : teams ) {

			final Team team = teamData.getTeam();

			session.persist( team );

			if ( teamData.getLogo() != null ) {
				teamLogoService.uploadLogo( team, teamData.getLogo() );
			}
		}
	}

	private void generateMatches( final Cup cup, final int count, final Session session, final MatchDataGenerationStrategy strategy ) {

		final Category category = cup.getCategory();

		for ( int i = 0; i < count; i++ ) {
			final Team team1 = getRandomTeam( category );
			final Team team2 = getRandomTeam( category );

			if ( team1 == null || team2 == null ) {
				continue;
			}

			if ( team1.getId() == team2.getId() ) {
				continue;
			}

			session.persist( generateNBAMatch( cup, team1, team2, strategy ) );
		}
	}

	private Match generateNBAMatch( final Cup cup, final Team team1, final Team team2, final MatchDataGenerationStrategy strategy ) {

		final Match match = new Match();

		match.setCup( cup );
		match.setTeam1( team1 );
		match.setScore1( strategy.generateScore() );
		match.setTeam2( team2 );
		match.setScore2( strategy.generateScore() );
		match.setBeginningTime( strategy.generateBeginningTime( dateTimeService ) );
		match.setMatchFinished( strategy.isFinished() );

		return match;
	}

	private int getRandomInt( final int minValue, final int maxValue ) {

		if ( maxValue == 0 ) {
			return 0;
		}

		return minValue + ( int ) ( Math.random() * ( maxValue - minValue + 1 ) );
	}

	private Team getRandomTeam( final Category category ) {
		final List<Team> teams = teamService.loadAll( category );

		if ( teams == null || teams.size() == 0 ) {
			return null;
		}

		return teams.get( getRandomInt( 0, teams.size() - 1 ) );
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

	private abstract class MatchDataGenerationStrategy {

		abstract Date generateBeginningTime( final DateTimeService dateTimeService );

		abstract int generateScore();

		abstract boolean isFinished();
	}
}
