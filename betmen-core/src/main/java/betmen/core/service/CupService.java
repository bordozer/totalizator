package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.SportKind;
import betmen.core.entity.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

    Cup loadAndAssertExists(int cupId);

    List<Cup> loadAllCurrent();

    List<Cup> loadPublic();

    List<Cup> loadPublicCurrent();

    List<Cup> loadPublicFinished();

    List<Cup> load(final Category category);

    List<Cup> loadPublic(final Category category);

    List<Cup> loadPublicCurrent(final Category category);

    List<Cup> loadPublicCurrent(final SportKind sportKind);

    List<Cup> loadPublicFinished(final Category category);

    List<Cup> loadPublic(final SportKind sportKind);

    List<Cup> loadHidden();

    List<Cup> loadHiddenCurrent();

    List<Cup> loadCups(final PointsCalculationStrategy strategy);

    boolean isCupStarted(final Cup cup);

    boolean isCupFinished(final Cup cup);

    boolean isCupPublic(final Cup cup);

    Comparator<Cup> categoryNameOrCupNameComparator();

    int cupsCountWithPointsStrategy(final int pcsId);

    int cupsCount(int categoryId);

    boolean exists(int cupId);

    List<Cup> loadAllTeamActivePublicCups(int teamId);

    List<Cup> getPublicCupsWhereUserMadeBetsOnDate(User user, LocalDate date);

    List<Cup> getCurrentPublicCupsOfUserFavoritesCategories(final User user);

    List<Cup> getUserCupsOnDate(LocalDate date, User user);
}
