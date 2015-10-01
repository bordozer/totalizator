package totalizator.app.services.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import totalizator.app.models.activities.events.MatchBetEvent;
import totalizator.app.models.activities.events.MatchEvent;
import totalizator.app.services.jobs.parameters.AbstractJobTaskParameters;
import totalizator.app.services.jobs.parameters.RemoteGamesImportJobTaskParameters;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;

@Service
public class JsonServiceImpl implements JsonService {

	@Override
	public String toJson( final Object o ) {
		return new Gson().toJson( o );
	}

	@Override
	public AbstractJobTaskParameters fromJson( final String json ) {
		return new Gson().fromJson( json, RemoteGamesImportJobTaskParameters.class );
	}

	@Override
	public MatchEvent fromMatchEventJson( final String json ) {
		return new Gson().fromJson( json, MatchEvent.class );
	}

	@Override
	public MatchBetEvent fromMatchBetEventJson( final String json ) {
		return new Gson().fromJson( json, MatchBetEvent.class );
	}

	@Override
	public GamesImportJobLogResultJSON fromJsonJobLogResultJSON( final String json ) {
		return new Gson().fromJson( json, GamesImportJobLogResultJSON.class );
	}
}
