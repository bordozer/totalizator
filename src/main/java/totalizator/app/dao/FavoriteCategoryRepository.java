package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.FavoriteCategory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FavoriteCategoryRepository implements FavoriteCategoryDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<FavoriteCategory> loadAll() {
		return em.createNamedQuery( FavoriteCategory.LOAD_ALL, FavoriteCategory.class )
				.getResultList();
	}

	@Override
	public FavoriteCategory load( final int id ) {
		return em.find( FavoriteCategory.class, id );
	}

	@Override
	public FavoriteCategory save( final FavoriteCategory entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	public List<FavoriteCategory> loadAllForUser( final int userId ) {
		return em.createNamedQuery( FavoriteCategory.LOAD_FOR_USER, FavoriteCategory.class )
				.setParameter( "userId", userId )
				.getResultList();
	}

	@Override
	public FavoriteCategory find( final int userId, final int categoryId ) {

		final List<FavoriteCategory> list = em.createNamedQuery( FavoriteCategory.LOAD_FOR_USER_AND_CATEGORY, FavoriteCategory.class )
				.setParameter( "userId", userId )
				.setParameter( "categoryId", categoryId )
				.getResultList();

		return list.size() == 1 ? list.get( 0 ) : null;
	}
}
