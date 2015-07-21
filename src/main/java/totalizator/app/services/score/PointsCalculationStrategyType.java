package totalizator.app.services.score;

public enum PointsCalculationStrategyType {

	BASKETBALL( 1, "Basketball" )
	, FOOTBALL( 2, "Football" );

	private final int id;
	private final String name;

	PointsCalculationStrategyType( int id, String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static PointsCalculationStrategyType getById( final int id ) {

		for ( final PointsCalculationStrategyType type : PointsCalculationStrategyType.values() ) {
			if ( type.getId() == id ) {
				return type;
			}
		}

		return null;
	}
}
