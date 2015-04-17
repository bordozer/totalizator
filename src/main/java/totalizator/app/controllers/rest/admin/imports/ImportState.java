package totalizator.app.controllers.rest.admin.imports;

import java.util.EnumSet;

public enum ImportState {

	NOT_STARTED( "Not started" )
	, IN_PROGRESS( "In progress" )
	, STOPPED( "Stopped by user" )
	, FINISHED( "Finished successfully" )
	, ERROR( "Finished with error" )
	;

	public static final EnumSet ACTIVE = EnumSet.of( IN_PROGRESS );
	public static final EnumSet INACTIVE = EnumSet.of( NOT_STARTED, STOPPED, FINISHED, ERROR );

	private final String description;

	ImportState( final String description ) {
		this.description = description;
	}

	public boolean isActive() {
		return ACTIVE.contains( this );
	}

	public String getDescription() {
		return description;
	}
}
