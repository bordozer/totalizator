package totalizator.app.services.utils;

import totalizator.app.models.activities.events.MatchBetEvent;
import totalizator.app.models.activities.events.MatchEvent;
import totalizator.app.services.jobs.parameters.AbstractJobTaskParameters;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;

public interface JsonService {

	// TODO: test
	String toJson( final Object o );

	// TODO: test
	AbstractJobTaskParameters fromJson( final String json );

	// TODO: test
	MatchEvent fromMatchEventJson( final String json );

	// TODO: test
	MatchBetEvent fromMatchBetEventJson( final String json );

	// TODO: test
	GamesImportJobLogResultJSON fromJsonJobLogResultJSON( final String json );
}
