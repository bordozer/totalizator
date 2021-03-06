package betmen.core.service.points;

import betmen.core.entity.UserGroupEntity;
import betmen.core.model.points.UserSummaryPointsHolder;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchPoints;
import betmen.core.entity.User;
import betmen.core.service.GenericService;

import java.time.LocalDate;
import java.util.List;

public interface MatchPointsService extends GenericService<MatchPoints> {

    String CACHE_QUERY = "totalizator.app.cache.matchPointsService";

    void delete(final Match match);

    void delete(final UserGroupEntity userGroupEntity);

    void delete(final Cup cup);

    List<MatchPoints> loadAll(final Match match);

    List<MatchPoints> loadAll(final Match match, final UserGroupEntity userGroupEntity);

    MatchPoints load(final User user, final Match match);

    MatchPoints load(final User user, final Match match, final UserGroupEntity userGroupEntity);

    UserSummaryPointsHolder load(final User user, final Cup cup);

    UserSummaryPointsHolder load(final User user, final Cup cup, final UserGroupEntity userGroupEntity);

    List<UserSummaryPointsHolder> getUsersRating(final LocalDate date);

    List<UserSummaryPointsHolder> getUsersRating(final LocalDate dateFrom, final LocalDate dateTo);

    UserSummaryPointsHolder getUserRating(final User user, final Cup cup, final LocalDate date);

    UserSummaryPointsHolder getUserRating(final User user, final Cup cup, final LocalDate dateFrom, final LocalDate dateTo);
}
