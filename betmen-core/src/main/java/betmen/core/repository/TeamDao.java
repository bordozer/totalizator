package betmen.core.repository;

import betmen.core.entity.Team;

import java.util.List;

public interface TeamDao {

    String CACHE_ENTRY = "totalizator.app.cache.team";
    String CACHE_QUERY = "totalizator.app.cache.teams";

    List<Team> loadAll();

    Team load(final int id);

    Team save(Team entry);

    void delete(final int id);
}
