package totalizator.app.dao;

import totalizator.app.models.Category;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import java.util.List;

public interface CategoryDao extends GenericService<Category>, NamedEntityGenericService<Category> {

	String CACHE_ENTRY = "totalizator.app.cache.category";
	String CACHE_QUERY = "totalizator.app.cache.categories";

	List<Category> loadAll( final int sportKindId );
}
