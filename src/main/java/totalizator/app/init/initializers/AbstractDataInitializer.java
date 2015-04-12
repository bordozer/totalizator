package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import totalizator.app.models.*;
import totalizator.app.services.LogoService;
import totalizator.app.services.utils.DateTimeService;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
	protected DateTimeService dateTimeService;

	@Autowired
	protected LogoService logoService;

	public void generate( final List<User> users, final Session session ) throws IOException, DocumentException {

		final Category category = generateCategory( getName(), session );
		uploadLogo( category, "nba-logo.png" );

		final List<Cup> cups = generateCups( category, session );

		final List<Team> teams = loadTeamsData( category, session );

		for ( final Cup cup : cups ) {

			final List<Match> finishedCupMatches = generateMatches( cup, teams, pastStrategy(), 100, session );
			generateBets( users, finishedCupMatches, session );

			final List<Match> futureCupMatches = generateMatches( cup, teams, futureStrategy(), 10, session );
			generateBets( users, futureCupMatches, session );

			generateCupBets( cup, teams, users, session );
		}
	}

	protected List<Team> createTeams( final List<TeamData> teams, final Session session ) throws IOException {

		final List<Team> result = newArrayList();

		for ( final TeamData teamData : teams ) {

			final Team team = teamData.getTeam();

			session.persist( team );

			if ( teamData.getLogo() != null ) {
				logoService.uploadLogo( team, teamData.getLogo() );
			}

			result.add( team );
		}

		return result;
	}

	protected List<Match> generateMatches( final Cup cup, final List<Team> teams, final MatchDataGenerationStrategy strategy, final int count, final Session session ) {

		final List<Match> result = newArrayList();

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

		final MatchDataGenerationStrategy matchDataGenerationStrategy = pastStrategy();

		for ( final User user : users ) {

			final int betsCountGenerateTo = rnd( 0, cupMatches.size() - 1 );

			if ( betsCountGenerateTo == 0 ) {
				continue;
			}

			final List<Match> matches = newArrayList( cupMatches );
			Collections.shuffle( matches );
			final Iterator<Match> iterator = matches.iterator();

			int i = 0;
			while ( iterator.hasNext() ) {
				final Match match = iterator.next();

				final MatchBet bet = new MatchBet();

				bet.setMatch( match );
				bet.setUser( user );
				bet.setBetScore1( matchDataGenerationStrategy.generateScore() );
				bet.setBetScore2( matchDataGenerationStrategy.generateScore() );
				bet.setBetTime( dateTimeService.minusHours( match.getBeginningTime(), rnd( 1, 12 ) ) );

				session.persist( bet );

				iterator.remove();

				i++;

				if ( i >= betsCountGenerateTo ) {
					break;
				}
			}

		}
	}

	private void generateCupBets( final Cup cup, final List<Team> teams, final List<User> users, final Session session ) {

		for ( final User user : users ) {

			final int betCount = rnd( 1, cup.getWinnersCount() );
			for ( int i = 1; i <= betCount; i++ ) {

				final CupTeamBet cupTeamBet = new CupTeamBet();

				cupTeamBet.setCup( cup );
				cupTeamBet.setUser( user );
				cupTeamBet.setTeam( getRandomTeam( teams ) );
				cupTeamBet.setCupPosition( i );
				cupTeamBet.setBetTime( dateTimeService.minusDays( cup.getCupStartTime(), rnd( 1, 10 ) ) );

				session.persist( cupTeamBet );
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

		return teams.get( rnd( 0, teams.size() - 1 ) );
	}

	private Category generateCategory( final String name, final Session session ) {

		final Category category = new Category( name );
		category.setLogoFileName( name.toLowerCase() );

		session.persist( category );

		return category;
	}

	public static int rnd( final int minValue, final int maxValue ) {

		if ( maxValue == 0 ) {
			return 0;
		}

		return minValue + ( int ) ( Math.random() * ( maxValue - minValue + 1 ) );
	}

	protected void uploadLogo( final Category category, final String logoFileName ) {
		try {
			logoService.uploadLogo( category, new File( String.format( "%s/%s", TeamImportService.RESOURCES_DIR, category.getCategoryName().toLowerCase() ), logoFileName ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	protected void uploadLogo( final Cup cup, final String logoFileName ) {
		try {
			logoService.uploadLogo( cup, new File( String.format( "%s/%s.png", TeamImportService.RESOURCES_DIR, cup.getCategory().getCategoryName().toLowerCase() ), logoFileName ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
