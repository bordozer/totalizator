package betmen.core.service;

import betmen.core.entity.AbstractEntity;

import java.util.List;

public interface GenericService<T extends AbstractEntity> {

    String CACHE_PERMANENT = "totalizator.app.cache.permanent";

    List<T> loadAll();

    T load(final int id);

    T save(T entry);

    void delete(final int id);
}
