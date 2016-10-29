package betmen.core.service.utils;

import betmen.core.entity.activities.events.MatchBetEvent;
import betmen.core.entity.activities.events.MatchEvent;
import betmen.core.service.jobs.parameters.AbstractJobTaskParameters;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;

public interface JsonService {

    // TODO: test
    String toJson(final Object o);

    // TODO: test
    AbstractJobTaskParameters fromJson(final String json);

    // TODO: test
    MatchEvent fromMatchEventJson(final String json);

    // TODO: test
    MatchBetEvent fromMatchBetEventJson(final String json);

    // TODO: test
    GamesImportJobLogResultJSON fromJsonJobLogResultJSON(final String json);
}
