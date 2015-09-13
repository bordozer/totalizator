package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.PointsCalculationStrategy;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadAllCurrent();

	List<Cup> loadPublic();

	List<Cup> loadPublicCurrent();

	List<Cup> loadPublicFinished();

	List<Cup> load( final Category category );

	List<Cup> loadPublic( final Category category );

	List<Cup> loadPublicFinished( final Category category );

	List<Cup> loadHidden();

	List<Cup> loadHiddenCurrent();

	List<Cup> loadCups( final PointsCalculationStrategy strategy );

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( final Cup cup );

	boolean isCupFinished( final Cup cup );

	boolean isCupPublic( final Cup cup );
}
