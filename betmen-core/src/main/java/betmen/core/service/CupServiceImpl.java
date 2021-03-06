package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.SportKind;
import betmen.core.entity.User;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.CupDao;
import betmen.core.repository.jpa.CupJpaRepository;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CupServiceImpl implements CupService {

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
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private LogoService logoService;

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
    public Cup load(final int cupId) {
        return cupRepository.load(cupId);
    }

    @Override
    @Transactional
    public void delete(final int cupId) {
        final Cup cup = loadAndAssertExists(cupId);
        cupTeamService.clearForCup(cupId);
        matchService.loadAll(cup).stream().forEach(match -> matchService.delete(match.getId()));
        userGroupService.loadAllHaveCup(cupId).stream().forEach(userGroupEntity -> userGroupService.delete(userGroupEntity.getId()));
        cupRepository.delete(cupId);
        try {
            logoService.deleteLogo(cup);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot delete logo of cup #%d", cupId), e);
        }
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
    public List<Cup> loadAllTeamActivePublicCups(int teamId) {
        return cupJpaRepository.loadAllTeamActivePublicCups(teamId);
    }

    @Override
    public List<Cup> getPublicCupsWhereUserMadeBetsOnDate(final User user, final LocalDate date) {
        String fromTime = dateTimeService.formatDateTimeSQL(dateTimeService.getFirstSecondOf(date));
        String toTime = dateTimeService.formatDateTimeSQL(dateTimeService.getLastSecondOf(date));
        return cupJpaRepository.findAllPublicCupsWhereUserMadeBetsOnDate(user.getId(), fromTime, toTime)
                .stream()
                .sorted(categoryNameOrCupNameComparator())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> getCurrentPublicCupsOfUserFavoritesCategories(final User user) {
        return cupJpaRepository.findAllCurrentPublicCupsOfUserFavoritesCategories(user.getId())
                .stream()
                .distinct()
                .sorted(categoryNameOrCupNameComparator())
                .collect(Collectors.toList());
    }

    @Override
    public List<Cup> getUserCupsOnDate(final User user, final LocalDate date) {
        String fromTime = dateTimeService.formatDateTimeSQL(dateTimeService.getFirstSecondOf(date));
        String toTime = dateTimeService.formatDateTimeSQL(dateTimeService.getLastSecondOf(date));
        List<Cup> currentCupsOfFavoriteCategories = cupJpaRepository.findAllPublicCupsOfUserFavoritesCategoriesWithMatchesOnDate(user.getId(), fromTime, toTime);
        List<Cup> publicCupsWhereUserMadeBetsOnDate = getPublicCupsWhereUserMadeBetsOnDate(user, date);

        List<Cup> matchesOfCupsToShow = new ArrayList<>();
        matchesOfCupsToShow.addAll(currentCupsOfFavoriteCategories);
        matchesOfCupsToShow.addAll(publicCupsWhereUserMadeBetsOnDate);

        return matchesOfCupsToShow.stream().distinct().collect(Collectors.toList());
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
