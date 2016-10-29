package betmen.core.repository.jpa;

import betmen.core.entity.SportKind;
import betmen.core.repository.SportKindDao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportKindJpaRepository extends JpaRepository<SportKind, Integer> {

    @Caching(evict = {
            @CacheEvict(value = SportKindDao.CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = SportKindDao.CACHE_QUERY, allEntries = true)
    })
    @Override
    void delete(Integer id);

    SportKind findBySportKindName(String name);
}
