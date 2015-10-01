package totalizator.app.services.jobs.results;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GamesImportJobLogResultJSON implements JobLogResultJSON {

	private int totalSteps;
	private int performedSteps;

	private List<ImportedRemoteGame> importedRemoteGames = newArrayList();

	@Override
	public int getTotalSteps() {
		return totalSteps;
	}

	@Override
	public void setTotalSteps( final int totalSteps ) {
		this.totalSteps = totalSteps;
	}

	@Override
	public int getPerformedSteps() {
		return performedSteps;
	}

	@Override
	public void setPerformedSteps( final int performedSteps ) {
		this.performedSteps = performedSteps;
	}

	public List<ImportedRemoteGame> getImportedRemoteGames() {
		return importedRemoteGames;
	}

	public void setImportedRemoteGames( final List<ImportedRemoteGame> importedRemoteGames ) {
		this.importedRemoteGames = importedRemoteGames;
	}

	public void addImportedRemoteGame( final ImportedRemoteGame importedRemoteGame ) {
		importedRemoteGames.add( importedRemoteGame );
	}
}
