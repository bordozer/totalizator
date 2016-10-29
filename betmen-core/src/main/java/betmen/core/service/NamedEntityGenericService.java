package betmen.core.service;

import betmen.core.entity.AbstractEntity;

public interface NamedEntityGenericService<T extends AbstractEntity> {

    T findByName(final String name);
}
