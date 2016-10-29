package betmen.core.repository;

import betmen.core.entity.Team;
import betmen.core.service.GenericService;

public interface TeamDao extends GenericService<Team> {
    String CACHE_ENTRY = "totalizator.app.cache.team";
    String CACHE_QUERY = "totalizator.app.cache.teams";
}
