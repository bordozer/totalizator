package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.*;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class NBA extends AbstractDataInitializer {

	private static final String CATEGORY = "NBA";

	private static final String CUP_2 = "2015 - playoff";
	private static final String CUP_1 = "2015 - regular";
	private static final String CUP_3 = "2014 - playoff";

	@Override
	protected String getName() {
		return CATEGORY;
	}

	@Override
	protected List<Cup> generateCups( final Category category, final List<Team> teams, final Session session ) {

		final PointsCalculationStrategy pointsStrategy = new PointsCalculationStrategy();
		pointsStrategy.setStrategyName( "NBA points calculation strategy" );
		pointsStrategy.setPointsForMatchScore( 6 );
		pointsStrategy.setPointsForMatchWinner( 1 );
		pointsStrategy.setPointsDelta( 3 );
		pointsStrategy.setPointsForBetWithinDelta( 3 );

		session.persist( pointsStrategy );

		return newArrayList(
				nba2014PlayOff_Finished( category, teams, pointsStrategy, session )
				, nba2015Regular( category, teams, pointsStrategy, session )
				, nba2015PlayOff( category, teams, pointsStrategy, session )
		);
	}

	@Override
	protected List<Team> loadTeamsData( final Category category, final Session session ) throws DocumentException, IOException {
		return createTeams( teamImportService.importNBA( category ), session );
	}

	@Override
	protected MatchDataGenerationStrategy pastStrategy() {
		return MatchDataGenerationStrategy.nbaPastStrategy();
	}

	@Override
	protected MatchDataGenerationStrategy futureStrategy() {
		return MatchDataGenerationStrategy.nbaFutureStrategy();
	}

	private Cup nba2015PlayOff( final Category category, final List<Team> teams, final PointsCalculationStrategy pointsStrategy, final Session session ) {

		final Cup nba2015PlayOff = new Cup( CUP_2, category );
		nba2015PlayOff.setPublicCup( true );
		nba2015PlayOff.setWinnersCount( 2 );
		nba2015PlayOff.setCupStartTime( dateTimeService.parseDate( "18/04/2015 00:00" ) );
		nba2015PlayOff.setPointsCalculationStrategy( pointsStrategy );

		session.persist( nba2015PlayOff );

		fillAliveTeams( teams, session, nba2015PlayOff );

		return nba2015PlayOff;
	}

	private Cup nba2015Regular( final Category category, final List<Team> teams, final PointsCalculationStrategy pointsStrategy, final Session session ) {

		final Cup nba2015Regular = new Cup( CUP_1, category );
		nba2015Regular.setPublicCup( true );
		nba2015Regular.setWinnersCount( 4 );
		nba2015Regular.setCupStartTime( dateTimeService.parseDate( "01/09/2014 00:00" ) );
		nba2015Regular.setPointsCalculationStrategy( pointsStrategy );

		session.persist( nba2015Regular );

		fillAliveTeams( teams, session, nba2015Regular );

		return nba2015Regular;
	}

	private Cup nba2014PlayOff_Finished( final Category category, final List<Team> teams, final PointsCalculationStrategy pointsStrategy, final Session session ) {

		final Cup nba2014PlayOff = new Cup( CUP_3, category );
		nba2014PlayOff.setPublicCup( true );
		nba2014PlayOff.setWinnersCount( 2 );
		nba2014PlayOff.setCupStartTime( dateTimeService.parseDate( "20/04/2014 00:00" ) );
		nba2014PlayOff.setPointsCalculationStrategy( pointsStrategy );

		session.persist( nba2014PlayOff );

		final Team teamWinner1 = getRandomTeam( teams );
		final CupWinner winner1 = new CupWinner();
		winner1.setCup( nba2014PlayOff );
		winner1.setTeam( teamWinner1 );
		winner1.setCupPosition( 1 );

		session.persist( winner1 );

		final Team teamWinner2 = getRandomTeam( teams );
		final CupWinner winner2 = new CupWinner();
		winner2.setCup( nba2014PlayOff );
		winner2.setTeam( teamWinner2 );
		winner2.setCupPosition( 2 );

		session.persist( winner2 );

		final CupTeam cupTeam1 = new CupTeam( nba2014PlayOff, teamWinner1 );
		session.persist( cupTeam1 );

		final CupTeam cupTeam2 = new CupTeam( nba2014PlayOff, teamWinner2 );
		session.persist( cupTeam2 );
		return nba2014PlayOff;
	}

	private void fillAliveTeams( final List<Team> teams, final Session session, final Cup nba2015PlayOff ) {

		final List<Team> aliveTeams = newArrayList();
		final int aliveTeamsCount = rnd( 4, teams.size() - 1 );
		for ( int i = 0; i < aliveTeamsCount; i++ ) {

			final Team randomTeam = getRandomTeam( teams );

			if ( aliveTeams.contains( randomTeam ) ) {
				continue;
			}

			final CupTeam cupTeam1 = new CupTeam( nba2015PlayOff, randomTeam );
			session.persist( cupTeam1 );

			aliveTeams.add( randomTeam );
		}
	}
}
