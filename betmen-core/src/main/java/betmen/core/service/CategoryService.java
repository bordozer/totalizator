package betmen.core.service;

import betmen.core.entity.Category;

import java.util.Comparator;
import java.util.List;

public interface CategoryService extends GenericService<Category>, NamedEntityGenericService<Category> {

    List<Category> loadAll(final int sportKindId);

    Category loadAndAssertExists(final int categoryId);

    Comparator<Category> categoriesByFavoritesByName(final List<Category> favoriteCategories);

    Category findByCategoryName(String categoryName);

    int categoriesCount(int sportKindId);
}
