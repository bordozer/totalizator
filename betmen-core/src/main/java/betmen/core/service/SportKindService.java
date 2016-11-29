package betmen.core.service;

import betmen.core.entity.SportKind;

import java.util.List;

public interface SportKindService {

    List<SportKind> loadAll();

    SportKind load(final int id);

    SportKind save(SportKind entry);

    void delete(final int id);

    SportKind findBySportKindName(String name);

    SportKind loadAndAssertExists(int sportKindId);
}
