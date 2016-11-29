package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.PointsCalculationStrategy;

import java.util.List;

public interface CupDao {

    String CACHE_ENTRY = "totalizator.app.cache.cup";
    String CACHE_QUERY = "totalizator.app.cache.cups";

    List<Cup> loadAll();

    Cup load(final int id);

    Cup save(Cup entry);

    void delete(final int id);

    Cup findByName(final String name);

    List<Cup> loadCups(final PointsCalculationStrategy strategy);
}
