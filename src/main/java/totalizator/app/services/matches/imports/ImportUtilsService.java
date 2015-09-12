package totalizator.app.services.matches.imports;

import totalizator.app.models.Cup;

public interface ImportUtilsService {

	GameImportStrategyType getGameImportStrategyType( final Cup cup );
}
