package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;

import java.util.List;

public interface CupWinnerService extends GenericService<CupWinner> {

	List<CupWinner> loadAll( final Cup cup );

	List<CupWinner> loadAll( final Cup cup, final Team team  );

	void saveAll( final Cup cup, List<CupWinner> winners );

	void saveAll( List<CupWinner> winners );

	void deleteAllWinners( Cup cup );

	boolean hasChampions( final Cup cup );
}
