package totalizator.app.services.activiries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import totalizator.app.models.activities.AbstractActivityStreamEntry;
import totalizator.app.models.activities.ActivityStreamEntryType;
import totalizator.app.services.matches.MatchService;

@Component
public class ActivityStreamValidator {

	@Autowired
	private MatchService matchService;

	public boolean validate( final AbstractActivityStreamEntry activity ) {
		return getInstance( activity.getActivityStreamEntryType() ).validate( activity );
	}

	AbstractActivityValidator getInstance( final ActivityStreamEntryType activityType ) {

		switch ( activityType ) {
			case MATCH_BET_CREATED:
			case MATCH_BET_CHANGED:
			case MATCH_BET_DELETED:
			case MATCH_FINISHED:
				return matchValidator();

			default:
				throw new IllegalArgumentException( String.format( "IUnsupported ActivityStreamEntryType: %s", activityType ) );
		}
	}

	private AbstractActivityValidator matchValidator() {

		return new AbstractActivityValidator() {

			@Override
			boolean validate( final AbstractActivityStreamEntry activity ) {
				return matchExists( activity );
			}

			private boolean matchExists( final AbstractActivityStreamEntry activity ) {
				return matchService.load( activity.getActivityEntryId() ) != null;
			}
		};
	}

	private abstract class AbstractActivityValidator {

		abstract boolean validate( final AbstractActivityStreamEntry activity );
	}
}
