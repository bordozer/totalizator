package betmen.core.service.matches;

import betmen.core.model.MatchSearchModel;
import betmen.core.entity.Match;

import java.util.List;

public interface MatchesAndBetsWidgetService {

    List<Match> loadAll(final MatchSearchModel model);
}
