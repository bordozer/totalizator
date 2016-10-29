package betmen.core.repository;

import betmen.core.entity.ActivityStreamEntry;
import betmen.core.service.GenericService;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityStreamDao extends GenericService<ActivityStreamEntry> {

    String CACHE_ENTRY = "totalizator.app.cache.activityStreamEntry";
    String CACHE_QUERY = "totalizator.app.cache.activityStreamEntries";

    ActivityStreamEntry loadByActivityEntryId(final int activityEntryId);

    List<ActivityStreamEntry> loadAllEarlierThen(final LocalDateTime time);

    List<ActivityStreamEntry> loadAllForMatch(final int matchId);

    List<ActivityStreamEntry> loadAllForUser(final int userId, final int qty);
}
