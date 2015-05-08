package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository implements CategoryDao {

	private static final Logger LOGGER = Logger.getLogger( CategoryRepository.class );

	private static final String CACHE_ENTRY = "totalizator.app.cache.category";
	private static final String CACHE_QUERY = "totalizator.app.cache.categories";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Category> loadAll() {
		return em.createNamedQuery( Category.LOAD_ALL, Category.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public Category load( final int id ) {
		return em.find( Category.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public Category save( final Category entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public Category findByName( final String categoryName ) {
		final List<Category> categories = em.createNamedQuery( Category.FIND_BY_NAME, Category.class )
				.setParameter( "categoryName", categoryName )
				.getResultList();

		return categories.size() == 1 ? categories.get( 0 ) : null;
	}
}
