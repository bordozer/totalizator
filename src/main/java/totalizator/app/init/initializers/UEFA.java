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
public class UEFA extends AbstractDataInitializer {

	private static final String CATEGORY = "UEFA";

	private static final String CUP_1 = "Euro 2016";
	private static final String CUP_2 = "World cup 2018";

	@Override
	protected String getSportKindName() {
		return "Football";
	}

	@Override
	protected String getName() {
		return CATEGORY;
	}

	@Override
	protected List<Cup> generateCups( final Category category, final List<Team> teams, final Session session ) {

		final PointsCalculationStrategy pointsStrategy = new PointsCalculationStrategy();
		pointsStrategy.setStrategyName( "UEFA points calculation strategy" );
		pointsStrategy.setPointsForMatchScore( 3 );
		pointsStrategy.setPointsForMatchWinner( 1 );

		session.persist( pointsStrategy );

		final Cup uefa2016Euro = new Cup( CUP_1, category );
		uefa2016Euro.setPublicCup( false );
		uefa2016Euro.setWinnersCount( 3 );
		uefa2016Euro.setCupStartTime( dateTimeService.parseDateTime( "10/06/2016 00:00" ) );
		uefa2016Euro.setLogoFileName( "uefa-euro-2016-logo.png" );
		uefa2016Euro.setPointsCalculationStrategy( pointsStrategy );

		session.persist( uefa2016Euro );

		uploadLogo( uefa2016Euro, "uefa-euro-2016-logo.png" );

		final Cup uefa2018WorldCup = new Cup( CUP_2, category );
		uefa2018WorldCup.setPublicCup( false );
		uefa2018WorldCup.setWinnersCount( 3 );
		uefa2018WorldCup.setCupStartTime( dateTimeService.parseDateTime( "14/06/2018 00:00" ) );
		uefa2018WorldCup.setLogoFileName( "uefa-world-cup-2018-logo.png" );
		uefa2018WorldCup.setPointsCalculationStrategy( pointsStrategy );

		session.persist( uefa2018WorldCup );

		uploadLogo( uefa2018WorldCup, "uefa-world-cup-2018-logo.png" );

		return newArrayList( uefa2016Euro, uefa2018WorldCup );
	}

	@Override
	protected List<Team> loadTeamsData( final Category category, final Session session ) throws DocumentException, IOException {
		return createTeams( teamImportService.importUEFA( category ), session );
	}

	@Override
	protected MatchDataGenerationStrategy pastStrategy() {
		return MatchDataGenerationStrategy.uefaPastStrategy();
	}

	@Override
	protected MatchDataGenerationStrategy futureStrategy() {
		return MatchDataGenerationStrategy.uefaFutureStrategy();
	}
}
