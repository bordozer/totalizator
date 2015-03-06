package totalizator.app.services;

import totalizator.app.models.AbstractEntity;

public interface NamedEntityGenericService<T extends AbstractEntity> {

	T findByName( final String name );
}
