package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.PointsCalculationStrategyDao;
import totalizator.app.models.PointsCalculationStrategy;

import java.util.List;

@Service
public class PointsCalculationStrategyServiceImpl implements PointsCalculationStrategyService {

	@Autowired
	private PointsCalculationStrategyDao pointsCalculationStrategyRepository;

	@Override
	@Transactional( readOnly = true )
	public List<PointsCalculationStrategy> loadAll() {
		return pointsCalculationStrategyRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public PointsCalculationStrategy load( final int id ) {
		return pointsCalculationStrategyRepository.load( id );
	}

	@Override
	@Transactional()
	public PointsCalculationStrategy save( final PointsCalculationStrategy entry ) {
		return pointsCalculationStrategyRepository.save( entry );
	}

	@Override
	@Transactional()
	public void delete( final int id ) {
		pointsCalculationStrategyRepository.delete( id );
	}
}
