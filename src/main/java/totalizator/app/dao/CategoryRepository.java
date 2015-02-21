package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository {

	private static final Logger LOGGER = Logger.getLogger( CategoryRepository.class );

	@PersistenceContext
	private EntityManager em;

	public void save( final Category category ) {
		em.merge( category );
	}

	public Category load( final int id ) {
		final List<Category> categories = em.createNamedQuery( Category.FIND_CATEGORY_BY_ID, Category.class )
				.setParameter( "id", id )
				.getResultList();

		return categories.size() == 1 ? categories.get( 0 ) : null;
	}

	public Category findByName( final String categoryName ) {
		final List<Category> categories = em.createNamedQuery( Category.FIND_CATEGORY_BY_NAME, Category.class )
				.setParameter( "categoryName", categoryName )
				.getResultList();

		return categories.size() == 1 ? categories.get( 0 ) : null;
	}

	public List<Category> loadAll() {
		return em.createNamedQuery( Category.LOAD_ALL_CATEGORIES, Category.class )
				.getResultList();
	}
}
