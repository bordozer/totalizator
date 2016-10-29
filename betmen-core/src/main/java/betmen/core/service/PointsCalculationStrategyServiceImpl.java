package betmen.core.service;

import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.PointsCalculationStrategyDao;
import betmen.core.repository.jpa.PointsCalculationStrategyJpaRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointsCalculationStrategyServiceImpl implements PointsCalculationStrategyService {

    private static final Logger LOGGER = Logger.getLogger(PointsCalculationStrategyServiceImpl.class);

    @Autowired
    private PointsCalculationStrategyDao pointsCalculationStrategyRepository;

    @Autowired
    private PointsCalculationStrategyJpaRepository pointsCalculationStrategyJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PointsCalculationStrategy> loadAll() {
        return pointsCalculationStrategyRepository.loadAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PointsCalculationStrategy load(final int id) {
        return pointsCalculationStrategyRepository.load(id);
    }

    @Override
    @Transactional()
    public PointsCalculationStrategy save(final PointsCalculationStrategy entry) {
        return pointsCalculationStrategyRepository.save(entry);
    }

    @Override
    @Transactional()
    public void delete(final int id) {
        if (!pointsCalculationStrategyJpaRepository.exists(id)) {
            throw new UnprocessableEntityException("Points calculation strategy does not exist");
        }
        pointsCalculationStrategyJpaRepository.delete(id);
    }

    @Override
    public PointsCalculationStrategy findByName(final String strategyName) {
        return pointsCalculationStrategyJpaRepository.findByStrategyName(strategyName);
    }

    @Override
    public PointsCalculationStrategy loadAndAssertExists(final int pcsId) {
        PointsCalculationStrategy strategy = load(pcsId);
        if (strategy == null) {
            LOGGER.warn(String.format("Cannot get Points strategy with ID: %d", pcsId));
            throw new UnprocessableEntityException("Points calculation strategy does not exist");
        }
        return strategy;
    }
}
