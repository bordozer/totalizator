package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dao.CategoryRepository;
import totalizator.app.models.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public void save( final Category category ) {
		categoryRepository.save( category );
	}

	@Override
	public Category load( final int id ) {
		return categoryRepository.load( id );
	}

	@Override
	public Category findByName( final String categoryName ) {
		return categoryRepository.findByName( categoryName );
	}
}
