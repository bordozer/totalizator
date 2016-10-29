package betmen.core.entity.activities;

import betmen.core.entity.ActivityStreamEntry;
import betmen.core.entity.activities.events.MatchEvent;

public class MatchActivityStreamEntry extends AbstractActivityStreamEntry {

    private final MatchEvent matchEvent;

    public MatchActivityStreamEntry(final ActivityStreamEntry activityStreamEntry, final MatchEvent matchEvent) {

        super(activityStreamEntry);

        this.matchEvent = matchEvent;
    }

    public MatchEvent getMatchEvent() {
        return matchEvent;
    }
}
