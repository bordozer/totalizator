package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.FavoriteCategoryDao;
import totalizator.app.models.Category;
import totalizator.app.models.FavoriteCategory;
import totalizator.app.models.User;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoriteCategoryServiceImpl implements FavoriteCategoryService {

	@Autowired
	private FavoriteCategoryDao favoriteCategoryRepository;

	@Override
	@Transactional
	public void addToFavorites( final User user, final Category category ) {

		if ( isInFavorites( user, category ) ) {
			return;
		}

		favoriteCategoryRepository.save( new FavoriteCategory( user, category ) );
	}

	@Override
	@Transactional
	public void removeFromFavorites( final User user, final Category category ) {

		if ( ! isInFavorites( user, category ) ) {
			return;
		}

		favoriteCategoryRepository.delete( favoriteCategoryRepository.find( user.getId(), category.getId() ).getId() );
	}

	@Override
	@Transactional( readOnly = true )
	public boolean isInFavorites( final User user, final Category category ) {
		return favoriteCategoryRepository.find( user.getId(), category.getId() ) != null;
	}

	@Override
	@Transactional( readOnly = true )
	public List<Category> loadUserFavoriteCategories( final User user ) {

		return favoriteCategoryRepository.loadAllForUser( user.getId() )
				.stream()
				.map( new Function<FavoriteCategory, Category>() {
					@Override
					public Category apply( final FavoriteCategory favoriteCategory ) {
						return favoriteCategory.getCategory();
					}
				} )
				.collect( Collectors.toList() )
		;
	}
}
