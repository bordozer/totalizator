package totalizator.app.services.matches.imports;

import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;

@Service
public class ImportUtilsServiceImpl implements ImportUtilsService {

	@Override
	public GameImportStrategyType getGameImportStrategyType( final Cup cup ) {
		return GameImportStrategyType.getById( cup.getCategory().getRemoteGameImportStrategyTypeId() );
	}
}
