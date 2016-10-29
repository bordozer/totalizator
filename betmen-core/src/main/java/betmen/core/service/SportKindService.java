package betmen.core.service;

import betmen.core.entity.SportKind;

public interface SportKindService extends GenericService<SportKind> {

    SportKind findBySportKindName(String name);

    SportKind loadAndAssertExists(int sportKindId);
}
