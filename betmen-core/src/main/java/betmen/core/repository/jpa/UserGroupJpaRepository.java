package betmen.core.repository.jpa;

import betmen.core.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupJpaRepository extends JpaRepository<UserGroupEntity, Integer> {

    // TODO: caching
    List<UserGroupEntity> findAllByOwnerIdOrderByGroupNameAsc(int userId);
}
