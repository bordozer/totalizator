package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import betmen.core.service.GenericService;

import java.util.List;

public interface CupWinnerDao extends GenericService<CupWinner> {

    String CACHE_ENTRY = "totalizator.app.cache.cup-winner";
    String CACHE_QUERY = "totalizator.app.cache.cup-winner.query";

    List<CupWinner> loadAll(final Cup cup);

    CupWinner load(final Cup cup, final Team team);

    void saveAll(final List<CupWinner> winners);
}
