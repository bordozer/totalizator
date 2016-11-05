package betmen.core.repository.specifications;

import betmen.core.entity.Match;
import betmen.core.model.MatchSearchModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MnBWidgetMatchesSpecification implements Specification<Match> {

    private final MatchSearchModel searchQuery;

    public MnBWidgetMatchesSpecification(final MatchSearchModel searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public Predicate toPredicate(final Root<Match> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
        return cb.and(
                buildCupCriteria(root, cb),
                buildDateCriteria(root, cb),
                buildFinishedOrFutureCriteria(root, cb),
                buildTeamCriteria(searchQuery.getTeamId(), root, cb),
                buildTeamCriteria(searchQuery.getTeam2Id(), root, cb)
        );
    }

    private Predicate buildDateCriteria(final Root<Match> root, final CriteriaBuilder cb) {
        LocalDate filterByDate = searchQuery.getFilterByDate();
        if (!searchQuery.isFilterByDateEnable() || filterByDate == null) {
            return cb.conjunction();
        }
        Timestamp fromTimeStamp = Timestamp.valueOf(LocalDateTime.of(filterByDate, LocalTime.of(0, 0, 0)));
        Timestamp toTimeStamp = Timestamp.valueOf(LocalDateTime.of(filterByDate, LocalTime.of(23, 59, 59)));
        return cb.between(root.get("beginningTime"), fromTimeStamp, toTimeStamp);
    }

    private Predicate buildCupCriteria(final Root<Match> root, final CriteriaBuilder cb) {
        return cb.equal(root.join("cup").get("id"), searchQuery.getCupId());
    }

    private Predicate buildFinishedOrFutureCriteria(final Root<Match> root, final CriteriaBuilder cb) {
        if (searchQuery.isShowFinished() && searchQuery.isShowFutureMatches()) {
            return cb.conjunction();
        }
        return cb.equal(root.get("matchFinished"), searchQuery.isShowFinished());
    }

    private Predicate buildTeamCriteria(final int teamId, final Root<Match> root, final CriteriaBuilder cb) {
        if (teamId == 0) {
            return cb.conjunction();
        }
        return cb.or(
                cb.equal(root.get("team1"), teamId),
                cb.equal(root.get("team2"), teamId)
        );
    }
}
