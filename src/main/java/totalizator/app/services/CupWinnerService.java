package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;

import java.util.List;

public interface CupWinnerService extends GenericService<CupWinner> {

	List<CupWinner> loadAll( final Cup cup );

	CupWinner load( final Cup cup, final Team team );

	List<CupWinner> loadAll( final Team team  );

	void saveAll( final List<CupWinner> winners );

	void deleteAllWinners( final Cup cup );

	boolean hasChampions( final Cup cup );
}
