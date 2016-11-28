package betmen.core.repository.jpa;

import betmen.core.entity.Cup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CupJpaRepository extends JpaRepository<Cup, Integer>, JpaSpecificationExecutor<Cup> {

    @Query(value = "select count(c) from Cup c where c.category.id=:categoryId")
    int cupsCount(@Param("categoryId") int categoryId);

    @Query(value = "select count(c) from Cup c where c.pointsCalculationStrategy.id=:pcsId")
    int cupsCountWithPointsStrategy(@Param("pcsId") int pcsId);

    @Query(value = "SELECT DISTINCT c.* " +
            " FROM cups c join matches as m on (c.id=m.cupId) " +
            " WHERE c.id NOT IN (SELECT cw.cupId FROM cupWinners cw) " +
            "   AND c.publicCup = true " +
            "   AND (m.team1Id=:teamId or m.team2Id=:teamId) " +
            " ORDER BY c.cupStartTime DESC "
            , nativeQuery = true)
    List<Cup> loadAllTeamActivePublicCups(@Param("teamId") int teamId);

    @Query(value = "SELECT cup.* " +
            " FROM cups cup JOIN categories cat ON (cup.categoryId = cat.id)" +
            " WHERE cup.publicCup = true" +
            "   AND cat.id IN (SELECT categoryId FROM favorites WHERE userId = :userId)" +
            "   AND cup.id NOT IN (SELECT cupId FROM cupWinners)"
            , nativeQuery = true)
    List<Cup> findAllCurrentPublicCupsOfUserFavoritesCategories(@Param("userId") int userId);

    @Query(value = "SELECT cup.*" +
            "  FROM cups cup " +
            " WHERE cup.publicCup = true " +
            "   AND cup.id IN ( " +
            "                     SELECT m.cupId " +
            "                     FROM matches m " +
            "                     WHERE (m.beginningTime BETWEEN :fromTime AND :toTime)" +
            "                       AND m.id IN ( " +
            "                                     SELECT mb.matchId FROM matchBets mb WHERE mb.userId = :userId " +
            "                                    ) " +
            "                     ) "
            , nativeQuery = true)
    List<Cup> findAllPublicCupsWhereUserMadeBetsOnDate(@Param("userId") int userId, @Param("fromTime") String fromTime, @Param("toTime") String toTime);
}
