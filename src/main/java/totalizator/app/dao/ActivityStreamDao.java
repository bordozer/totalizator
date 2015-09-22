package totalizator.app.dao;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.services.GenericService;

import java.util.List;

public interface ActivityStreamDao extends GenericService<ActivityStreamEntry> {

	String CACHE_ENTRY = "totalizator.app.cache.activityStreamEntry";
	String CACHE_QUERY = "totalizator.app.cache.activityStreamEntries";

	List<ActivityStreamEntry> loadAll( final int userId );

	ActivityStreamEntry loadByActivityEntryId( final int activityEntryId );
}
