package betmen.core.repository;

import betmen.core.entity.SportKind;
import betmen.core.service.GenericService;

public interface SportKindDao extends GenericService<SportKind> {

    String CACHE_ENTRY = "totalizator.app.cache.sportKind";
    String CACHE_QUERY = "totalizator.app.cache.sportKinds";
}

