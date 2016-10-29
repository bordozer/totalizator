package betmen.core.service;

import betmen.core.entity.Team;

import java.util.List;

public interface TeamService extends GenericService<Team> {

    List<Team> loadAll(final int categoryId);

    Team loadAndAssertExists(int teamId);

    Team findByName(final int categoryId, final String teamName);

    Team findByImportId(final int categoryId, final String importId);

    boolean exists(int teamId);
}
