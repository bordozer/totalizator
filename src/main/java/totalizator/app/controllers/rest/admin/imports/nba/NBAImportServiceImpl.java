package totalizator.app.controllers.rest.admin.imports.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.imports.GamesDataImportMonitor;
import totalizator.app.services.RemoteContentService;

@Service
public class NBAImportServiceImpl implements NBAImportService {

	@Autowired
	private RemoteContentService remoteContentService;

	private final GamesDataImportMonitor gamesDataImportMonitor = new GamesDataImportMonitor();

	@Override
	public void startImport() {
		setActivity( true );
	}

	@Override
	public void stopImport() {
		setActivity( false );
	}

	@Override
	public boolean isImportingNow() {
		return gamesDataImportMonitor.isImportActive();
	}

	private void setActivity( final boolean importActive ) {
		synchronized ( gamesDataImportMonitor ) {
			gamesDataImportMonitor.setImportActive( importActive );
		}
	}
}
