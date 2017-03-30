package betmen.core.repository.jpa;

import betmen.core.entity.MatchMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchMessageJpaRepository extends JpaRepository<MatchMessageEntity, Integer> {

    List<MatchMessageEntity> findAllByMatchId(int matchId);

    List<MatchMessageEntity> findAllByUserId(int userId);

    MatchMessageEntity findAllByIdAndUserId(int id, int userId);

    void deleteOneByIdAndUserId(int messageId, int id);
}
