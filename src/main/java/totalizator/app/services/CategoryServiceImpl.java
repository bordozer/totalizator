package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CategoryDao;
import totalizator.app.models.Category;

import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryRepository;

	@Override
	@Transactional( readOnly = true )
	public List<Category> loadAll() {
		return newArrayList( categoryRepository.loadAll() );
	}

	@Override
	@Transactional
	public Category save( final Category category ) {
		return categoryRepository.save( category );
	}

	@Override
	@Transactional( readOnly = true )
	public Category load( final int id ) {
		return categoryRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		categoryRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public Category findByName( final String categoryName ) {
		return categoryRepository.findByName( categoryName );
	}

	@Override
	public List<Category> loadAll( final int sportKindId ) {
		return categoryRepository.loadAll( sportKindId );
	}

	@Override
	public Comparator<Category> categoriesByFavoritesByName( final List<Category> favoriteCategories ) {

		return new Comparator<Category>() {
			@Override
			public int compare( final Category o1, final Category o2 ) {

				if ( favoriteCategories.contains( o1 ) && favoriteCategories.contains( o2 ) ) {
					return o1.getCategoryName().compareToIgnoreCase( o2.getCategoryName() );
				}

				if ( favoriteCategories.contains( o1 ) ) {
					return -1;
				}

				if ( favoriteCategories.contains( o2 ) ) {
					return 1;
				}

				return o1.getCategoryName().compareToIgnoreCase( o2.getCategoryName() );
			}
		};
	}
}
