package betmen.core.repository;

import betmen.core.entity.Category;
import betmen.core.service.GenericService;
import betmen.core.service.NamedEntityGenericService;

import java.util.List;

public interface CategoryDao extends GenericService<Category>, NamedEntityGenericService<Category> {

    String CACHE_ENTRY = "totalizator.app.cache.category";
    String CACHE_QUERY = "totalizator.app.cache.categories";

    List<Category> loadAll(final int sportKindId);
}
