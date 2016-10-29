package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.GenericService;

import java.util.List;

public interface CupTeamBetDao extends GenericService<CupTeamBet> {

    String CACHE_ENTRY = "totalizator.app.cache.user-cup-winner-bet";
    String CACHE_QUERY = "totalizator.app.cache.user-cup-winner-bet.query";

    List<CupTeamBet> load(final Cup cup, final User user);

    CupTeamBet load(final Cup cup, final User user, final int cupPosition);

    CupTeamBet load(final Cup cup, final Team team, final User user);

    List<CupTeamBet> load(final Cup cup);

    void clearForCup(final Cup cup);
}
