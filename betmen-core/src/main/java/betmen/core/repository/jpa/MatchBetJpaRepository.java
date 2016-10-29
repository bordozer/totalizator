package betmen.core.repository.jpa;

import betmen.core.entity.MatchBet;
import betmen.core.repository.MatchBetDao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchBetJpaRepository extends JpaRepository<MatchBet, Integer> {

    @Caching(evict = {
            @CacheEvict(value = MatchBetDao.CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
    })
    int deleteByUserIdAndId(final int userId, final int id);
}
