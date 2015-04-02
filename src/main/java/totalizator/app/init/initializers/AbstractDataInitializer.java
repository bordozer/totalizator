package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import totalizator.app.models.*;
import totalizator.app.services.TeamLogoService;
import totalizator.app.services.utils.DateTimeService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractDataInitializer {

	protected abstract String getName();

	protected abstract List<Cup> generateCups( final Category category, final Session session );

	protected abstract List<Team> loadTeamsData( final Category category, final Session session ) throws DocumentException, IOException;

	protected abstract MatchDataGenerationStrategy pastStrategy();

	protected abstract MatchDataGenerationStrategy futureStrategy();

	@Autowired
	protected TeamImportService teamImportService;

	@Autowired
	private TeamLogoService teamLogoService;

	@Autowired
	private DateTimeService dateTimeService;

	public void generate( final List<User> users, final Session session ) throws IOException, DocumentException {

		final Category category = generateCategory( getName(), session );

		final List<Cup> cups = generateCups( category, session );

		final List<Team> teams = loadTeamsData( category, session );

		for ( final Cup cup : cups ) {

			final List<Match> finishedCupMatches = generateMatches( cup, teams, pastStrategy(), session );
			generateBets( users, finishedCupMatches, session );

			final List<Match> futureCupMatches = generateMatches( cup, teams, futureStrategy(), session );
			generateBets( users, futureCupMatches, session );
		}
	}

	protected List<Team> createTeams( final List<TeamData> teams, final Session session ) throws IOException {

		final List<Team> result = newArrayList();

		for ( final TeamData teamData : teams ) {

			final Team team = teamData.getTeam();

			session.persist( team );

			if ( teamData.getLogo() != null ) {
				teamLogoService.uploadLogo( team, teamData.getLogo() );
			}

			result.add( team );
		}

		return result;
	}

	protected List<Match> generateMatches( final Cup cup, final List<Team> teams, final MatchDataGenerationStrategy strategy, final Session session ) {

		final int count = 100;

		final List<Match> result = newArrayList();

		final Category category = cup.getCategory();

		for ( int i = 0; i < count; i++ ) {
			final Team team1 = getRandomTeam( teams );
			final Team team2 = getRandomTeam( teams );

			if ( team1 == null || team2 == null ) {
				continue;
			}

			if ( team1.getId() == team2.getId() ) {
				continue;
			}

			final Match match = initMatch( cup, team1, team2, strategy );
			session.persist( match );

			result.add( match );
		}

		return result;
	}

	private void generateBets( final List<User> users, final List<Match> cupMatches, final Session session ) {

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
				bet.setBetScore1( pastStrategy().generateScore() );
				bet.setBetScore2( pastStrategy().generateScore() );
				bet.setBetTime( dateTimeService.offset( match.getBeginningTime(), Calendar.HOUR, getRandomInt( 1, 12 ) ) );

				session.persist( bet );

				i++;

				if ( i >= betsCountGenerateTo ) {
					break;
				}
			}
		}
	}

	private Match initMatch( final Cup cup, final Team team1, final Team team2, final MatchDataGenerationStrategy strategy ) {

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

	private Team getRandomTeam( final List<Team> teams ) {

		if ( teams == null || teams.size() == 0 ) {
			return null;
		}

		return teams.get( getRandomInt( 0, teams.size() - 1 ) );
	}

	private Category generateCategory( final String name, final Session session ) {
		final Category category = new Category( name );
		session.persist( category );

		return category;
	}

	public static int getRandomInt( final int minValue, final int maxValue ) {

		if ( maxValue == 0 ) {
			return 0;
		}

		return minValue + ( int ) ( Math.random() * ( maxValue - minValue + 1 ) );
	}
}
