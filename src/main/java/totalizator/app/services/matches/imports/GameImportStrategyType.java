package totalizator.app.services.matches.imports;

import java.util.EnumSet;

public enum GameImportStrategyType {

	NO_IMPORT( 0, "Inaccessible" )
	, NBA( 1, "NBA" )
	, UEFA( 2, "UEFA" )
	, NHL( 3, "NHL" )
	;

	final public static EnumSet<GameImportStrategyType> CUP_ID_NEEDED = EnumSet.<GameImportStrategyType>of( UEFA );

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

	public static GameImportStrategyType getById( final int id ) {

		for ( final GameImportStrategyType type : GameImportStrategyType.values() ) {
			if ( type.getId() == id ) {
				return type;
			}
		}

		throw new IllegalArgumentException( String.format( "Unsupported GameImportStrategyType ID: %d", id ) );
	}
}
