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
public class UEFA extends AbstractDataInitializer {

	private static final String CATEGORY = "UEFA";

	private static final String CUP_1 = "Euro 2016";
	private static final String CUP_2 = "World cup 2018";

	@Override
	protected String getName() {
		return CATEGORY;
	}

	@Override
	protected List<Cup> generateCups( final Category category, final Session session ) {

		final Cup uefa2016Euro = new Cup( CUP_1, category );
		uefa2016Euro.setShowOnPortalPage( true );
		uefa2016Euro.setWinnersCount( 3 );
		uefa2016Euro.setReadyForCupBets( false );
		uefa2016Euro.setReadyForMatchBets( false );
		uefa2016Euro.setCupStartTime( dateTimeService.parseDate( "2016-06-10" ) );
		uefa2016Euro.setFinished( false );

		session.persist( uefa2016Euro );

		final Cup uefa2018WorldCup = new Cup( CUP_2, category );
		uefa2018WorldCup.setWinnersCount( 3 );
		uefa2018WorldCup.setReadyForCupBets( false );
		uefa2018WorldCup.setReadyForMatchBets( false );
		uefa2018WorldCup.setCupStartTime( dateTimeService.parseDate( "2018-06-14" ) );
		uefa2018WorldCup.setFinished( false );

		session.persist( uefa2018WorldCup );

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
