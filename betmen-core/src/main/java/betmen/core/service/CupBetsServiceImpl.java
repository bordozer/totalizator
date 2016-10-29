package betmen.core.service;

import betmen.core.model.ErrorCodes;
import betmen.core.model.ValidationResult;
import betmen.core.repository.CupTeamBetDao;
import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.utils.DateTimeService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CupBetsServiceImpl implements CupBetsService {


    @Autowired
    private CupService cupService;

    @Autowired
    private CupTeamBetDao cupTeamBetRepository;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    @Transactional(readOnly = true)
    public List<CupTeamBet> loadAll() {
        return Lists.newArrayList(cupTeamBetRepository.loadAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CupTeamBet load(final int id) {
        return cupTeamBetRepository.load(id);
    }

    @Override
    @Transactional
    public CupTeamBet save(final CupTeamBet entry) {
        return cupTeamBetRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        cupTeamBetRepository.delete(id);
    }

    @Override
    public List<CupTeamBet> load(final Cup cup) {
        return Lists.newArrayList(cupTeamBetRepository.load(cup));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CupTeamBet> load(final Cup cup, final User user) {
        return Lists.newArrayList(cupTeamBetRepository.load(cup, user));
    }

    @Override
    public CupTeamBet load(final Cup cup, final User user, final int cupPosition) {
        return cupTeamBetRepository.load(cup, user, cupPosition);
    }

    @Override
    public CupTeamBet load(final Cup cup, final User user, final Team team) {
        return cupTeamBetRepository.load(cup, team, user);
    }

    @Override
    public boolean isCupBettingFinished(final Cup cup) {
        return cupService.isCupStarted(cup);
    }

    @Override
    public boolean isMatchBettingFinished(final Cup cup) {
        return cupService.isCupFinished(cup);
    }

    @Override
    public ValidationResult validateBettingAllowed(final Cup cup) {

        final Language language = translatorService.getDefaultLanguage();

        if (cupService.isCupFinished(cup)) {
            return ValidationResult.fail(ErrorCodes.CUP_FINISHED, translatorService.translate("Cup $1 finished", language, cup.getCupName()));
        }

        if (isCupBettingFinished(cup)) {
            return ValidationResult.fail(ErrorCodes.CUP_BETTING_FINISHED, translatorService.translate("Cup betting is not allowed after cup start ( $1 )", language, dateTimeService.formatDateTimeUI(cup.getCupStartTime())));
        }

        return ValidationResult.pass();
    }

    @Override
    public boolean canCupBeBet(final Cup cup, final User user) {
        return validateBettingAllowed(cup).isPassed();
    }

    @Override
    public void clearForCup(final Cup cup) {
        cupTeamBetRepository.clearForCup(cup);
    }
}
