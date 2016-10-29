package betmen.core.entity;

import betmen.core.entity.activities.ActivityStreamEntryType;
import betmen.core.entity.converters.ActivityStreamEntryTypeConverter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static betmen.core.entity.ActivityStreamEntry.LOAD_ALL;
import static betmen.core.entity.ActivityStreamEntry.LOAD_ALL_EARLIER_THEN;
import static betmen.core.entity.ActivityStreamEntry.LOAD_ALL_FOR_MATCH;
import static betmen.core.entity.ActivityStreamEntry.LOAD_ALL_FOR_USER;
import static betmen.core.entity.ActivityStreamEntry.LOAD_BY_ACTIVITY_ENTRY_ID;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "activities")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from ActivityStreamEntry c order by activityTime desc"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_USER,
                query = "select c from ActivityStreamEntry c where userId= :userId order by activityTime desc"
        ),
        @NamedQuery(
                name = LOAD_BY_ACTIVITY_ENTRY_ID,
                query = "select c from ActivityStreamEntry c where activityEntryId= :activityEntryId order by activityTime desc"
        ),
        @NamedQuery(
                name = LOAD_ALL_EARLIER_THEN,
                query = "select c from ActivityStreamEntry c where activityTime >= :activityTime order by activityTime desc"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_MATCH,
                query = "select c from ActivityStreamEntry c where activityEntryId = :activityEntryId and activityStreamEntryType in ( 1, 2, 3, 4 )  order by activityTime desc"
        )
})
public class ActivityStreamEntry extends AbstractEntity {

    public static final String LOAD_ALL = "activityStreamEntries.loadAll";
    public static final String LOAD_ALL_FOR_USER = "activityStreamEntries.loadAllForUser";
    public static final String LOAD_BY_ACTIVITY_ENTRY_ID = "activityStreamEntries.loadByActivityEntryId";
    public static final String LOAD_ALL_EARLIER_THEN = "activityStreamEntries.loadAllEarlierThen";
    public static final String LOAD_ALL_FOR_MATCH = "activityStreamEntries.loadAllForMatch";

    @ManyToOne
    @JoinColumn(name = "userId")
    private User activityOfUser;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime activityTime;

    @Convert(converter = ActivityStreamEntryTypeConverter.class)
    private ActivityStreamEntryType activityStreamEntryType;

    private int activityEntryId;

    private String eventJson;

    public ActivityStreamEntry() {
    }

    public ActivityStreamEntry(final User activityOfUser, final LocalDateTime activityTime) {
        this.activityOfUser = activityOfUser;
        this.activityTime = activityTime;
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

    public String getEventJson() {
        return eventJson;
    }

    public void setEventJson(final String eventJson) {
        this.eventJson = eventJson;
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ActivityStreamEntry)) {
            return false;
        }

        final ActivityStreamEntry activityStreamEntry = (ActivityStreamEntry) obj;
        return activityStreamEntry.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %d", activityStreamEntryType, activityEntryId);
    }
}
