package totalizator.app.services.jobs.executors;

import java.util.EnumSet;

public enum JobExecutionState {

	IDLE( 1 )
	, PREPARING_FOR_RUNNING( 6 )
	, IN_PROGRESS( 2 )
	, STOPPED_BY_USER( 3 )
	, FINISHED( 4 )
	, ERROR( 5 )
	;

	public static final EnumSet<JobExecutionState> RUNNING_STATES = EnumSet.of( IN_PROGRESS );
	public static final EnumSet<JobExecutionState> STOPPED_STATES = EnumSet.of( STOPPED_BY_USER, ERROR, FINISHED );

	private final int id;

	JobExecutionState( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static JobExecutionState getById( final int id ) {

		for ( final JobExecutionState activityStreamEntryType : values() ) {
			if ( activityStreamEntryType.getId() == id ) {
				return activityStreamEntryType;
			}
		}

		return IDLE;
	}
}
