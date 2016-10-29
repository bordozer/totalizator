package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeam;
import betmen.core.entity.Team;

import java.util.List;

public interface CupTeamService {

    List<CupTeam> loadAll(final int cupId);

    void saveCupTeam(final int cupId, final int teamId, final boolean isActive);

    boolean exists(final int cupId, final int teamId);

    boolean exists(final Cup cup, final Team team);

    List<Team> loadActiveForCup(final int cupId);

    void clearFor(final int teamId);

    void clearForCup(final int cupId);

    void clearFor(final int cupId, final int teamId);
}
