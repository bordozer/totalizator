package totalizator.app.controllers.rest.admin.imports.nba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.services.RemoteContentService;

@Service
public class NBAImportServiceImpl implements NBAImportService {

	@Autowired
	private RemoteContentService remoteContentService;

	@Override
	public void startImport() {

	}

	@Override
	public void stopImport() {

	}

	@Override
	public boolean isImportingNow() {
		return false;
	}
}
