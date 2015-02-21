package totalizator.app.services;

import totalizator.app.models.Category;

public interface CategoryService {
	void save( Category category );

//	Category load( final int id );

	Category findByName( final String categoryName );
}
