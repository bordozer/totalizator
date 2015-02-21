package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CategoryRepository;
import totalizator.app.models.Category;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	@Transactional
	public void save( final Category category ) {
		categoryRepository.save( category );
	}

	@Override
	@Transactional( readOnly = true )
	public Category load( final int id ) {
		return categoryRepository.load( id );
	}

	@Override
	@Transactional( readOnly = true )
	public Category findByName( final String categoryName ) {
		return categoryRepository.findByName( categoryName );
	}

	@Override
	@Transactional( readOnly = true )
	public List<Category> loadAll() {
		return categoryRepository.loadAll();
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		categoryRepository.delete( id );
	}
}
