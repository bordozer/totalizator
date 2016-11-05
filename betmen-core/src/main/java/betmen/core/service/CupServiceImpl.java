package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.SportKind;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.CupDao;
import betmen.core.repository.jpa.CupJpaRepository;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CupServiceImpl implements CupService {

    private static final Logger LOGGER = Logger.getLogger(CupServiceImpl.class);

    public static final Comparator<Cup> SORT_BY_CUP_BEGINNING_TIME_ASC = (o1, o2) -> o1.getCupStartTime().compareTo(o2.getCupStartTime());
    public static final Comparator<Cup> SORT_BY_CUP_BEGINNING_TIME_DESC = SORT_BY_CUP_BEGINNING_TIME_ASC.reversed();

    @Autowired
    private CupDao cupRepository;

    @Autowired
    private CupJpaRepository cupJpaRepository;

    @Autowired
    private CupWinnerService cupWinnerService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private CupTeamService cupTeamService;

    @Autowired
    private CupBetsService cupBetsService;

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public Cup loadAndAssertExists(final int cupId) {
        Cup cup = load(cupId);
        if (cup == null) {
            LOGGER.warn(String.format("Cannot get cup with ID: %d", cupId));
            throw new UnprocessableEntityException("Cup does not exist");
        }
        return cup;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cup> loadAll() {
        return cupRepository.loadAll().stream()
                .sorted(SORT_BY_CUP_BEGINNING_TIME_DESC)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Cup load(final int id) {
        return cupRepository.load(id);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        if (!cupJpaRepository.exists(id)) {
            throw new UnprocessableEntityException("Cup does not exist");
        }

        final Cup cup = load(id);

        cupTeamService.clearForCup(id);                            // delete assigned teams

        cupBetsService.clearForCup(cup);                            // delete all cup winners user's bets

        final List<Match> matches = matchService.loadAll(cup);    // delete all matches ( and bets )
        for (final Match match : matches) {
            matchService.delete(match.getId());
        }

        cupRepository.delete(id);                                    // delete the cup
    }

    @Override
    @Transactional(readOnly = true)
    public Cup findByName(final String name) {
        return cupRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cup> loadAllCurrent() {
        return loadAll()
                .stream()
                .filter(isCupCurrent())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublic() {
        return loadAll()
                .stream()
                .filter(isCupPublic())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublicCurrent() {
        return loadPublic()
                .stream()
                .filter(isCupCurrent())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublicFinished() {
        return loadPublic()
                .stream()
                .filter(isCupFinished())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> load(final Category category) {
        return loadAll()
                .stream()
                .filter(forCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublic(final Category category) {
        return loadPublic()
                .stream()
                .filter(forCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublicCurrent(final Category category) {
        return loadPublicCurrent()
                .stream()
                .filter(forCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublicCurrent(final SportKind sportKind) {
        return loadPublicCurrent()
                .stream()
                .filter(forSportKind(sportKind))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublicFinished(final Category category) {
        return loadPublicFinished()
                .stream()
                .filter(forCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadPublic(final SportKind sportKind) {
        return loadPublic()
                .stream()
                .filter(forSportKind(sportKind))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadHidden() {
        return loadAll()
                .stream()
                .filter(isCupHidden())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadHiddenCurrent() {
        return loadHidden()
                .stream()
                .filter(isCupCurrent())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadCups(final PointsCalculationStrategy strategy) {
        return cupRepository.loadCups(strategy);
    }

    @Override
    @Transactional
    public Cup save(final Cup entry) {
        return cupRepository.save(entry);
    }

    @Override
    public boolean isCupStarted(final Cup cup) {
        return dateTimeService.getNow().isAfter(cup.getCupStartTime());
    }

    @Override
    public boolean isCupFinished(final Cup cup) {
        return cupWinnerService.hasChampions(cup);
    }

    @Override
    public boolean isCupPublic(final Cup cup) {
        return isCupPublic().test(cup);
    }

    @Override
    public Comparator<Cup> categoryNameOrCupNameComparator() {
        return (cup1, cup2) -> {

            final Category category1 = cup1.getCategory();
            final Category category2 = cup2.getCategory();

            if (category1.getSportKind().equals(category2.getSportKind())) {
                return category1.getCategoryName().compareToIgnoreCase(category2.getCategoryName());
            }

            return category1.getSportKind().getSportKindName().compareToIgnoreCase(category2.getSportKind().getSportKindName());
        };
    }

    @Override
    public int cupsCountWithPointsStrategy(final int pcsId) {
        return cupJpaRepository.cupsCountWithPointsStrategy(pcsId);
    }

    @Override
    public int cupsCount(final int categoryId) {
        return cupJpaRepository.cupsCount(categoryId);
    }

    @Override
    public boolean exists(final int cupId) {
        return cupJpaRepository.exists(cupId);
    }

    @Override
    public List<Cup> loadAllTeamActiveCups(int teamId) {
        return cupJpaRepository.loadAllTeamActiveCups(teamId);
    }

    private Predicate<Cup> isCupCurrent() {
        return cup -> !isCupFinished(cup);
    }

    private Predicate<Cup> isCupPublic() {
        return Cup::isPublicCup;
    }

    private Predicate<Cup> isCupFinished() {
        return isCupCurrent().negate();
    }

    private Predicate<Cup> isCupHidden() {
        return isCupPublic().negate();
    }

    private Predicate<Cup> forCategory(final Category category) {
        return cup -> cup.getCategory().equals(category);
    }

    private Predicate<Cup> forSportKind(final SportKind sportKind) {
        return cup -> cup.getCategory().getSportKind().equals(sportKind);
    }
}
