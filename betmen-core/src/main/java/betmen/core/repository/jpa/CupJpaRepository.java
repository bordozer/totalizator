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
}
