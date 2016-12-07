package betmen.core.repository.jpa;

import betmen.core.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchJpaRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

    @Query(value = "select count(m) from Match m where m.cup.id=:cupId")
    int matchesCount(@Param("cupId") int cupId);

    @Query(value = "select m from Match m where (m.team1.id = :teamId or m.team2.id = :teamId) and m.beginningTime >= :from and m.beginningTime <= :to")
    List<Match> loadAllTeamMatchesForPeriod(@Param("teamId") int teamId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
