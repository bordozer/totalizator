package betmen.core.repository.jpa;

import betmen.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {

    Category findByCategoryName(String categoryName);

    @Query(value = "select count(c) from Category c where c.sportKind.id=:sportKind")
    int categoriesCount(@Param("sportKind") int sportKind);
}
