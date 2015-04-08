package totalizator.app.enums;

public enum CupPosition {

	FIRST( 0, "", 1 )
	, SECOND( 0, "", 2 )
	, THIRD( 0, "", 3 )
	;

	private final int id;
	private final String name;
	private final int order;

	CupPosition( final int id, final String name, final int order ) {
		this.id = id;
		this.name = name;
		this.order = order;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public CupPosition getById( final int id ) {
		for ( final CupPosition cupPosition : values() ) {
			if ( cupPosition.getId() == id ) {
				return cupPosition;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal TeamBetEventType id: %d", id ) );
	}
}
