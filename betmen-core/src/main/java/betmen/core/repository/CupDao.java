package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.service.GenericService;
import betmen.core.service.NamedEntityGenericService;

import java.util.List;

public interface CupDao extends GenericService<Cup>, NamedEntityGenericService<Cup> {

    String CACHE_ENTRY = "totalizator.app.cache.cup";
    String CACHE_QUERY = "totalizator.app.cache.cups";

    List<Cup> loadCups(final PointsCalculationStrategy strategy);
}
