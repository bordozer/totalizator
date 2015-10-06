package totalizator.app.services.matches;

import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;

import java.util.List;

public interface MatchesAndBetsWidgetService {

	List<Match> loadAll( final MatchesBetSettingsDTO dto );
}
