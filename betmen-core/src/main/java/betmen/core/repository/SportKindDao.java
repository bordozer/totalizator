package betmen.core.repository;

import betmen.core.entity.SportKind;

import java.util.List;

public interface SportKindDao {

    String CACHE_ENTRY = "totalizator.app.cache.sportKind";
    String CACHE_QUERY = "totalizator.app.cache.sportKinds";

    List<SportKind> loadAll();

    SportKind load(final int id);

    SportKind save(SportKind entry);

    void delete(final int id);
}

