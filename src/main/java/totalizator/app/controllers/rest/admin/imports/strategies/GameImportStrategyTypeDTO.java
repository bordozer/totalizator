package totalizator.app.controllers.rest.admin.imports.strategies;

public class GameImportStrategyTypeDTO {

	private final int id;
	private final String name;

	public GameImportStrategyTypeDTO( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format( "#%d, %s", id, name );
	}
}
