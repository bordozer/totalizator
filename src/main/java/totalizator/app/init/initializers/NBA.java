package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class NBA extends AbstractDataInitializer {

	private static final String CATEGORY = "NBA";

	private static final String CUP_1 = "2015 - regular";
	private static final String CUP_2 = "2015 - playoff";

	@Override
	protected String getName() {
		return CATEGORY;
	}

	@Override
	protected List<Cup> generateCups( final Category category, final Session session ) {

		final Cup nba2015Regular = new Cup( CUP_1, category );
		nba2015Regular.setPublicCup( true );
		nba2015Regular.setWinnersCount( 4 );
		nba2015Regular.setCupStartTime( dateTimeService.parseDate( "01/09/2014 00:00" ) );

		session.persist( nba2015Regular );

		final Cup nba2015PlayOff = new Cup( CUP_2, category );
		nba2015PlayOff.setPublicCup( true );
		nba2015PlayOff.setWinnersCount( 2 );
		nba2015PlayOff.setCupStartTime( dateTimeService.parseDate( "18/04/2015 00:00" ) );

		session.persist( nba2015PlayOff );

		return newArrayList( nba2015PlayOff, nba2015Regular );
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
