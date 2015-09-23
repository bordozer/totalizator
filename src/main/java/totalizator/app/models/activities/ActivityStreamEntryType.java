package totalizator.app.models.activities;

public enum ActivityStreamEntryType {

	MATCH_BET_CREATED( 1 )
	, MATCH_BET_CHANGED( 2 )
	, MATCH_BET_DELETED( 3 )
	, MATCH_FINISHED( 4 )
	;

	private final int id;

	ActivityStreamEntryType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ActivityStreamEntryType getById( final int id ) {

		for ( final ActivityStreamEntryType activityStreamEntryType : ActivityStreamEntryType.values() ) {
			if ( activityStreamEntryType.getId() == id ) {
				return activityStreamEntryType;
			}
		}

		return null;
	}
}
