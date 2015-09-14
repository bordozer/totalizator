package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CategoryDao;
import totalizator.app.models.Category;

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
}
