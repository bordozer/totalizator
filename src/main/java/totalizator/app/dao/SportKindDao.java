package totalizator.app.dao;

import totalizator.app.models.SportKind;
import totalizator.app.services.GenericService;

public interface SportKindDao extends GenericService<SportKind> {

	String CACHE_ENTRY = "totalizator.app.cache.sportKind";
	String CACHE_QUERY = "totalizator.app.cache.sportKinds";
}

