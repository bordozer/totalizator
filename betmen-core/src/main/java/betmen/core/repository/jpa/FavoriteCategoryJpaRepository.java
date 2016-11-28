package betmen.core.repository.jpa;

import betmen.core.entity.FavoriteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCategoryJpaRepository extends JpaRepository<FavoriteCategory, Integer> {

    void deleteAllByCategoryId(int categoryId);
}
