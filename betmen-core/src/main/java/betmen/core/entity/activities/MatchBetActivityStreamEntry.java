package betmen.core.entity.activities;

import betmen.core.entity.ActivityStreamEntry;
import betmen.core.entity.activities.events.MatchBetEvent;

public class MatchBetActivityStreamEntry extends AbstractActivityStreamEntry {

    private final MatchBetEvent matchBetEvent;

    public MatchBetActivityStreamEntry(final ActivityStreamEntry activityStreamEntry, final MatchBetEvent matchBetEvent) {

        super(activityStreamEntry);

        this.matchBetEvent = matchBetEvent;
    }

    public MatchBetEvent getMatchBetEvent() {
        return matchBetEvent;
    }
}
