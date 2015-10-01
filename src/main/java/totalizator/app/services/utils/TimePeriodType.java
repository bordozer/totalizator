package totalizator.app.services.utils;

public enum TimePeriodType {

	DATE_RANGE( 1 )
	, DAYS_OFFSET( 2 )
	, MONTH_OFFSET( 3 )
	;

	private final int id;

	TimePeriodType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TimePeriodType getById( final int id ) {
		for ( final TimePeriodType language : values() ) {
			if ( language.getId() == id ) {
				return language;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal TimePeriodType id: %d", id ) );
	}
}
