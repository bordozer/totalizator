package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;

import java.util.List;

public interface CupWinnerService extends GenericService<CupWinner> {

    List<CupWinner> loadAll(final Cup cup);

    CupWinner load(final Cup cup, final Team team);

    List<CupWinner> loadAll(final Team team);

    void saveAll(final List<CupWinner> winners);

    void deleteAllWinners(final Cup cup);

    boolean hasChampions(final Cup cup);
}
