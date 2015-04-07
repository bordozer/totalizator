package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.*;

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
		session.persist( uefa2016Euro );

		final Cup uefa2018WorldCup = new Cup( CUP_2, category );
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
