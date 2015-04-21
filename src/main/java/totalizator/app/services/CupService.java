package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadAllCurrent();

	void sort( List<Cup> cups );

	List<Cup> loadAllPublic();

	List<Cup> loadAllFinished();

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( Cup cup );

	boolean isCupFinished( Cup cup );
}
