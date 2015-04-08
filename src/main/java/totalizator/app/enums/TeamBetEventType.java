package totalizator.app.enums;

public enum TeamBetEventType {

	CUP_FIRST_PLACE( 0, "", 1 )
	, CUP_SECOND_PLACE( 0, "", 2 )
	, CUP_THIRD_PLACE( 0, "", 3 )
	;

	private final int id;
	private final String name;
	private final int order;

	TeamBetEventType( final int id, final String name, final int order ) {
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

	public TeamBetEventType getById( final int id ) {
		for ( final TeamBetEventType teamBetEventType : values() ) {
			if ( teamBetEventType.getId() == id ) {
				return teamBetEventType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal TeamBetEventType id: %d", id ) );
	}
}
