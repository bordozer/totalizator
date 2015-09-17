package totalizator.app.services;

import totalizator.app.models.*;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadAllCurrent();

	List<Cup> loadPublic();

	List<Cup> loadPublicCurrent();

	List<Cup> loadPublicFinished();

	List<Cup> load( final Category category );

	List<Cup> loadPublic( final Category category );

	List<Cup> loadPublicCurrent( final Category category );

	List<Cup> loadPublicCurrent( final SportKind sportKind );

	List<Cup> loadPublicFinished( final Category category );

	List<Cup> loadPublic( final SportKind sportKind );

	List<Cup> loadHidden();

	List<Cup> loadHiddenCurrent();

	List<Cup> loadCups( final PointsCalculationStrategy strategy );

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( final Cup cup );

	boolean isCupFinished( final Cup cup );

	boolean isCupPublic( final Cup cup );
}
