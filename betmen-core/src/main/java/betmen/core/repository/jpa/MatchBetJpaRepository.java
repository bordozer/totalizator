package betmen.core.repository.jpa;

import betmen.core.entity.MatchBet;
import betmen.core.repository.MatchBetDao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatchBetJpaRepository extends JpaRepository<MatchBet, Integer> {

    @Caching(evict = {
            @CacheEvict(value = MatchBetDao.CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
    })
    int deleteByUserIdAndId(final int userId, final int id);

    @Query(value = "select count(mb.id) " +
            " from matchBets mb " +
            "  join matches m on (mb.matchId = m.id) " +
            "  join cups cup on (m.cupId = cup.id) " +
            " where mb.userId = :userId " +
            "   and cup.publicCup = true " +
            "   and cup.categoryId = :categoryId " +
            "   and m.beginningTime between :fromTime and :toTime",
            nativeQuery = true)
    int getBetsCount(@Param("userId") int userId, @Param("categoryId") int categoryId, @Param("fromTime") String fromTime, @Param("toTime") String toTime);
}
