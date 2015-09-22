package totalizator.app.models.activities;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.User;

import java.time.LocalDateTime;

public abstract class AbstractActivityStreamEntry {

	protected User activityOfUser;
	protected LocalDateTime activityTime;
	protected ActivityStreamEntryType activityStreamEntryType;
	protected int activityEntryId;

	public AbstractActivityStreamEntry( final ActivityStreamEntry activityStreamEntry ) {
		this.activityOfUser = activityStreamEntry.getActivityOfUser();
		this.activityTime = activityStreamEntry.getActivityTime();
		this.activityStreamEntryType = activityStreamEntry.getActivityStreamEntryType();
		this.activityEntryId = activityStreamEntry.getActivityEntryId();
	}

	public User getActivityOfUser() {
		return activityOfUser;
	}

	public void setActivityOfUser( final User activityOfUser ) {
		this.activityOfUser = activityOfUser;
	}

	public LocalDateTime getActivityTime() {
		return activityTime;
	}

	public void setActivityTime( final LocalDateTime activityTime ) {
		this.activityTime = activityTime;
	}

	public ActivityStreamEntryType getActivityStreamEntryType() {
		return activityStreamEntryType;
	}

	public void setActivityStreamEntryType( final ActivityStreamEntryType activityStreamEntryType ) {
		this.activityStreamEntryType = activityStreamEntryType;
	}

	public int getActivityEntryId() {
		return activityEntryId;
	}

	public void setActivityEntryId( final int activityEntryId ) {
		this.activityEntryId = activityEntryId;
	}
}
