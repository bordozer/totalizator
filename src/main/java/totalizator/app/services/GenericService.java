package totalizator.app.services;

import totalizator.app.models.AbstractEntity;

import java.util.List;

public interface GenericService<T extends AbstractEntity> {

	List<T> loadAll();

	T save( T entry );

	T load( final int id );

	void delete( final int id );

	T findByName( final String name );
}
