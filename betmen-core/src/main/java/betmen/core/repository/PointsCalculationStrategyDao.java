package betmen.core.repository;

import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.service.GenericService;

public interface PointsCalculationStrategyDao extends GenericService<PointsCalculationStrategy> {

    String CACHE_ENTRY = "totalizator.app.cache.points-calculation-strategy";
    String CACHE_QUERY = "totalizator.app.cache.points-calculation-strategy.query";

}
