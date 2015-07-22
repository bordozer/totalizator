package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadAllCurrent();

	void sort( final List<Cup> cups );

	List<Cup> loadAllPublic();

	List<Cup> loadAllPublicFinished();

	List<Cup> loadAllPublic( final Category category );

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( final Cup cup );

	boolean isCupFinished( final Cup cup );
}
