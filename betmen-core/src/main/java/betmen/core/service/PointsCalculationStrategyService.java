package betmen.core.service;

import betmen.core.entity.PointsCalculationStrategy;

public interface PointsCalculationStrategyService extends GenericService<PointsCalculationStrategy> {

    PointsCalculationStrategy findByName(String strategyName);

    PointsCalculationStrategy loadAndAssertExists(int pcsId);
}
