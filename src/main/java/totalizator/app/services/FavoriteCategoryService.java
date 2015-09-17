package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.User;

import java.util.List;

public interface FavoriteCategoryService {

	void addToFavorites( final User user, final Category category );

	void removeFromFavorites( final User user, final Category category );

	boolean isInFavorites( final User user, final Category category );

	List<Category> loadUserFavoriteCategories( final User user );
}
