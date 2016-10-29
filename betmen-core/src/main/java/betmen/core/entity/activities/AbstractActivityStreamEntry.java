package betmen.core.entity.activities;

import betmen.core.entity.ActivityStreamEntry;
import betmen.core.entity.User;

import java.time.LocalDateTime;

public abstract class AbstractActivityStreamEntry {

    private int id;

    protected User activityOfUser;
    protected LocalDateTime activityTime;
    protected ActivityStreamEntryType activityStreamEntryType;
    protected int activityEntryId;

    public AbstractActivityStreamEntry(final ActivityStreamEntry activityStreamEntry) {

        this.id = activityStreamEntry.getId();

        this.activityOfUser = activityStreamEntry.getActivityOfUser();
        this.activityTime = activityStreamEntry.getActivityTime();
        this.activityStreamEntryType = activityStreamEntry.getActivityStreamEntryType();
        this.activityEntryId = activityStreamEntry.getActivityEntryId();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public User getActivityOfUser() {
        return activityOfUser;
    }

    public void setActivityOfUser(final User activityOfUser) {
        this.activityOfUser = activityOfUser;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(final LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }

    public ActivityStreamEntryType getActivityStreamEntryType() {
        return activityStreamEntryType;
    }

    public void setActivityStreamEntryType(final ActivityStreamEntryType activityStreamEntryType) {
        this.activityStreamEntryType = activityStreamEntryType;
    }

    public int getActivityEntryId() {
        return activityEntryId;
    }

    public void setActivityEntryId(final int activityEntryId) {
        this.activityEntryId = activityEntryId;
    }

    @Override
    public int hashCode() {
        return id * 31;
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        final AbstractActivityStreamEntry entry = (AbstractActivityStreamEntry) obj;
        return entry.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("#%d: %s ( %s )", id, activityOfUser, activityStreamEntryType);
    }
}
