package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository implements GenericService<Category>, NamedEntityGenericService<Category> {

	private static final Logger LOGGER = Logger.getLogger( CategoryRepository.class );
	private static final String CACHE_CATEGORY = "totalizator.app.cache.category";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_CATEGORY )
	public List<Category> loadAll() {
		return em.createNamedQuery( Category.LOAD_ALL, Category.class )
				.getResultList();
	}

	@Override
	@CacheEvict( value = CACHE_CATEGORY, allEntries = true )
	public Category save( final Category entry ) {
		return em.merge( entry );
	}

	@Override
	@Cacheable( value = CACHE_CATEGORY )
	public Category load( final int id ) {
		return em.find( Category.class, id );
	}

	@Override
	@CacheEvict( value = CACHE_CATEGORY, allEntries = true )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_CATEGORY )
	public Category findByName( final String categoryName ) {
		final List<Category> categories = em.createNamedQuery( Category.FIND_BY_NAME, Category.class )
				.setParameter( "categoryName", categoryName )
				.getResultList();

		return categories.size() == 1 ? categories.get( 0 ) : null;
	}
}
