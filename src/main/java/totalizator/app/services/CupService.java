package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> loadCurrent();

	List<Cup> loadPassed();

	Cup save( final Cup cup, final List<CupWinner> winners );

	boolean isCupStarted( Cup cup );

	boolean isCupFinished( Cup cup );
}
