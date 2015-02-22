package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository implements GenericService<Category> {

	private static final Logger LOGGER = Logger.getLogger( CategoryRepository.class );

	@PersistenceContext
	private EntityManager em;

	public List<Category> loadAll() {
		return em.createNamedQuery( Category.LOAD_ALL, Category.class )
				.getResultList();
	}

	public void save( final Category category ) {
		em.merge( category );
	}

	public Category load( final int id ) {
		return em.find( Category.class, id );
	}

	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	public Category findByName( final String categoryName ) {
		final List<Category> categories = em.createNamedQuery( Category.FIND_BY_NAME, Category.class )
				.setParameter( "categoryName", categoryName )
				.getResultList();

		return categories.size() == 1 ? categories.get( 0 ) : null;
	}
}
