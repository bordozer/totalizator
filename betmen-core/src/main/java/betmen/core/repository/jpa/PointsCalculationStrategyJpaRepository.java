package betmen.core.repository.jpa;

import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.repository.PointsCalculationStrategyDao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsCalculationStrategyJpaRepository extends JpaRepository<PointsCalculationStrategy, Integer> {

    @Caching(evict = {
            @CacheEvict(value = PointsCalculationStrategyDao.CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = PointsCalculationStrategyDao.CACHE_QUERY, allEntries = true)
    })
    @Override
    void delete(Integer id);

    PointsCalculationStrategy findByStrategyName(String name);
}
