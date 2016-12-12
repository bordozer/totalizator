package betmen.core.repository.jpa;

import betmen.core.entity.ActivityStreamEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityStreamJpaRepository extends JpaRepository<ActivityStreamEntry, Integer> {
}
