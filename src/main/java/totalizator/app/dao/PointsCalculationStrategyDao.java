package totalizator.app.dao;

import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.services.GenericService;

public interface PointsCalculationStrategyDao extends GenericService<PointsCalculationStrategy> {

	String CACHE_ENTRY = "totalizator.app.cache.points-calculation-strategy";
	String CACHE_QUERY = "totalizator.app.cache.points-calculation-strategy.query";

}
