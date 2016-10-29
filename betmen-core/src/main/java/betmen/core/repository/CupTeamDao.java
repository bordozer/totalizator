package betmen.core.repository;

import betmen.core.entity.CupTeam;
import betmen.core.service.GenericService;

import java.util.List;

public interface CupTeamDao extends GenericService<CupTeam> {

    List<CupTeam> loadAll(final int cupId);

    CupTeam load(final int cupId, final int teamId);
}
