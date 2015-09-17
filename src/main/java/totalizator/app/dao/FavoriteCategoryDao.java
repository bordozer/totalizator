package totalizator.app.dao;

import totalizator.app.models.FavoriteCategory;
import totalizator.app.services.GenericService;

import java.util.List;

public interface FavoriteCategoryDao extends GenericService<FavoriteCategory> {

	List<FavoriteCategory> loadAllForUser( int userId );

	FavoriteCategory find( int userId, int categoryId );
}
