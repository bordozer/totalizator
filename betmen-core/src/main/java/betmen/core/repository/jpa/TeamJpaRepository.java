package betmen.core.repository.jpa;

import betmen.core.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team, Integer> {

    List<Team> findAllByCategoryIdOrderByTeamNameAsc(int categoryId);

    Team findByCategoryIdAndTeamName(int categoryId, String teamName);

    Team findByCategoryIdAndImportId(int categoryId, String importId);
}
