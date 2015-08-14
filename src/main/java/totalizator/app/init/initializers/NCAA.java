package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.models.Team;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class NCAA extends AbstractDataInitializer {

	private static final String CATEGORY = "NCAA";

	private static final String CUP_1 = "2015 - regular";

	@Override
	protected String getName() {
		return CATEGORY;
	}

	@Override
	protected List<Cup> generateCups( final Category category, final List<Team> teams, final Session session ) {

		final PointsCalculationStrategy pointsStrategy = new PointsCalculationStrategy();
		pointsStrategy.setStrategyName( "NCAA points calculation strategy" );
		pointsStrategy.setPointsForMatchScore( 4 );
		pointsStrategy.setPointsForMatchWinner( 1 );
		pointsStrategy.setPointsDelta( 3 );
		pointsStrategy.setPointsForBetWithinDelta( 2 );

		session.persist( pointsStrategy );

		final Cup ncaa2015 = new Cup( CUP_1, category );
		ncaa2015.setPublicCup( true );
		ncaa2015.setWinnersCount( 8 );
		ncaa2015.setCupStartTime( dateTimeService.parseDateTime( "01/09/2014 00:00" ) );
		ncaa2015.setPointsCalculationStrategy( pointsStrategy );

		session.persist( ncaa2015 );

		return newArrayList( ncaa2015 );
	}

	@Override
	protected List<Team> loadTeamsData( final Category category, final Session session ) throws DocumentException, IOException {
		return createTeams( teamImportService.importNCAA( category ), session );
	}

	@Override
	protected MatchDataGenerationStrategy pastStrategy() {
		return MatchDataGenerationStrategy.ncaaPastStrategy();
	}

	@Override
	protected MatchDataGenerationStrategy futureStrategy() {
		return MatchDataGenerationStrategy.ncaaFutureStrategy();
	}
}
