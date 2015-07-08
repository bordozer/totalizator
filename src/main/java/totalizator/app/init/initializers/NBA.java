package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;

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

		final Cup nba2015PlayOff = new Cup( CUP_2, category );
		nba2015PlayOff.setPublicCup( true );
		nba2015PlayOff.setWinnersCount( 2 );
		nba2015PlayOff.setCupStartTime( dateTimeService.parseDate( "18/04/2015 00:00" ) );

		session.persist( nba2015PlayOff );

		final Cup nba2015Regular = new Cup( CUP_1, category );
		nba2015Regular.setPublicCup( true );
		nba2015Regular.setWinnersCount( 4 );
		nba2015Regular.setCupStartTime( dateTimeService.parseDate( "01/09/2014 00:00" ) );

		session.persist( nba2015Regular );

		final Cup nba2014PlayOff = new Cup( CUP_3, category );
		nba2014PlayOff.setPublicCup( true );
		nba2014PlayOff.setWinnersCount( 2 );
		nba2014PlayOff.setCupStartTime( dateTimeService.parseDate( "20/04/2014 00:00" ) );

		session.persist( nba2014PlayOff );

		final CupWinner winner1 = new CupWinner();
		winner1.setCup( nba2014PlayOff );
		winner1.setTeam( getRandomTeam( teams ) );
		winner1.setCupPosition( 1 );

		session.persist( winner1 );

		final CupWinner winner2 = new CupWinner();
		winner2.setCup( nba2014PlayOff );
		winner2.setTeam( getRandomTeam( teams ) );
		winner2.setCupPosition( 2 );

		session.persist( winner2 );

		return newArrayList( nba2014PlayOff, nba2015Regular, nba2015PlayOff );
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
}
