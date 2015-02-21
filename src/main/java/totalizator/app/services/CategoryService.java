package totalizator.app.services;

import totalizator.app.models.Category;

import java.util.List;

public interface CategoryService {

	void save( Category category );

	Category load( final int id );

	Category findByName( final String categoryName );

	List<Category> loadAll();

	void delete( final int id );
}
