package betmen.core.service;

import betmen.core.model.ValidationResult;
import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;

import java.util.List;

public interface CupBetsService extends GenericService<CupTeamBet> {

    List<CupTeamBet> load(final Cup cup);

    List<CupTeamBet> load(final Cup cup, final User user);

    CupTeamBet load(final Cup cup, final User user, final int cupPosition);

    CupTeamBet load(final Cup cup, final User user, final Team team);

    boolean isCupBettingFinished(final Cup cup);

    boolean isMatchBettingFinished(final Cup cup);

    ValidationResult validateBettingAllowed(final Cup cup);

    boolean canCupBeBet(final Cup cup, final User user);

    void clearForCup(final Cup cup);
}
