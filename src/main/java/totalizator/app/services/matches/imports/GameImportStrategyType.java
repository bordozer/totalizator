package totalizator.app.services.matches.imports;

public enum GameImportStrategyType {

	NBA( 1, "NBA" );

	private final int id;
	private final String name;

	GameImportStrategyType( int id, String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
