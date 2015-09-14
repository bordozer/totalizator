package totalizator.app.services;

import totalizator.app.models.Category;

import java.util.List;

public interface CategoryService extends GenericService<Category>, NamedEntityGenericService<Category> {

	List<Category> loadAll( final int sportKindId );
}
