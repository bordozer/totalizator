package betmen.core.service.utils;

import betmen.core.entity.activities.events.MatchBetEvent;
import betmen.core.entity.activities.events.MatchEvent;
import betmen.core.service.jobs.parameters.AbstractJobTaskParameters;
import betmen.core.service.jobs.parameters.RemoteGamesImportJobTaskParameters;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class JsonServiceImpl implements JsonService {

    @Override
    public String toJson(final Object o) {
        return new Gson().toJson(o);
    }

    @Override
    public AbstractJobTaskParameters fromJson(final String json) {
        return new Gson().fromJson(json, RemoteGamesImportJobTaskParameters.class);
    }

    @Override
    public MatchEvent fromMatchEventJson(final String json) {
        return new Gson().fromJson(json, MatchEvent.class);
    }

    @Override
    public MatchBetEvent fromMatchBetEventJson(final String json) {
        return new Gson().fromJson(json, MatchBetEvent.class);
    }

    @Override
    public GamesImportJobLogResultJSON fromJsonJobLogResultJSON(final String json) {
        return new Gson().fromJson(json, GamesImportJobLogResultJSON.class);
    }
}
