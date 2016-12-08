package betmen.core.repository.jpa;

import betmen.core.entity.CupTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CupTeamJpaRepository extends JpaRepository<CupTeam, Integer> {

    void deleteAllByTeamId(int teamId);
}
