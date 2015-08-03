package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.PointsCalculationStrategy;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadAllCurrent();

	void sort( final List<Cup> cups );

	List<Cup> loadAllPublic();

	List<Cup> loadAllPublic( final Category category );

	List<Cup> loadAllPublicFinished();

	List<Cup> loadAllPublicFinished( final Category category );

	List<Cup> loadCups( final PointsCalculationStrategy strategy );

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( final Cup cup );

	boolean isCupFinished( final Cup cup );
}
