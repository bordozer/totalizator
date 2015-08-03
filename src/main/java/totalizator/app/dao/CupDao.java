package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import java.util.List;

public interface CupDao extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	String CACHE_ENTRY = "totalizator.app.cache.cup";
	String CACHE_QUERY = "totalizator.app.cache.cups";

	List<Cup> loadCups( final PointsCalculationStrategy strategy );
}
