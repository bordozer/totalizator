package totalizator.app.init.initializers;

import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import totalizator.app.models.*;

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
	protected List<Cup> generateCups( final Category category, final Session session ) {

		final Cup ncaa2015 = new Cup( CUP_1, category );
		ncaa2015.setShowOnPortalPage( true );
		session.persist( ncaa2015 );

		return newArrayList( ncaa2015 );
	}

	@Override
	protected List<Team> generateTeams( final Category category, final Session session ) throws DocumentException, IOException {
		return createTeams( teamImportService.importNCAA( category ), session );
	}

	/*@Override
	protected List<Match> generateMatches( final Cup cup, final List<Team> teams, final Session session, final MatchDataGenerationStrategy strategy ) {
		return null;
	}*/

	/*@Override
	protected List<MatchBet> generateBets( final Match match, final User user, final Session session ) {
		return null;
	}*/

	@Override
	protected MatchDataGenerationStrategy pastStrategy() {
		return MatchDataGenerationStrategy.ncaaPastStrategy();
	}

	@Override
	protected MatchDataGenerationStrategy futureStrategy() {
		return MatchDataGenerationStrategy.ncaaFutureStrategy();
	}
}