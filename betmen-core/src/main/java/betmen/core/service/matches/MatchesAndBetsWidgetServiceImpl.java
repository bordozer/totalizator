package betmen.core.service.matches;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.exception.BadRequestException;
import betmen.core.model.MatchSearchModel;
import betmen.core.service.CupService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MatchesAndBetsWidgetServiceImpl implements MatchesAndBetsWidgetService {

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public List<Match> loadAll(final MatchSearchModel model) {
        Assert.assertTrue(model.getCupId() > 0, "Cup ID should be provided");
        Cup cup = cupService.loadAndAssertExists(model.getCupId());
        if (model.isFilterByDateEnable() && !dateTimeService.isValidDate(model.getFilterByDate())) {
            throw new BadRequestException(String.format("Date '%s' is invalid", model.getFilterByDate()));
        }

        List<Match> matches = getMatches(model);

        if (!model.isShowFutureMatches()) {
            matches = filterOutNotFinished(matches);
        }

        if (!model.isShowFinished()) {
            matches = filterOutFinished(matches);
        }

        if (model.getTeamId() > 0) {
            matches = filterByTeam(matches, model.getTeamId());
        }

        if (model.getTeam2Id() > 0) {
            matches = filterByTeam(matches, model.getTeam2Id());
        }

        final boolean isSortingByBeginningTimeAsc = model.getSorting() == 1;
        if (isSortingByBeginningTimeAsc) {
            matches = sortByBeginningTimeAsc(matches);
        } else {
            matches = sortByBeginningTimeDesc(matches);
        }

        return matches;
    }

    private List<Match> getMatches(final MatchSearchModel model) {

        if (model.isFilterByDateEnable()) {
            return matchService.loadAllOnDate(model.getCupId(), dateTimeService.parseDate(model.getFilterByDate()));
        }

        final boolean showNotFinishedMatchesOnly = model.isShowFutureMatches() && !model.isShowFinished();
        if (showNotFinishedMatchesOnly) {
            return matchService.loadAllNotFinished(model.getCupId());
        }

        if (model.getTeamId() > 0 && model.getTeam2Id() > 0) {
            return matchService.loadAll(model.getCupId(), model.getTeamId(), model.getTeam2Id());
        }

        if (model.getTeamId() > 0) {
            return matchService.loadAll(model.getCupId(), model.getTeamId());
        }

        if (model.getTeam2Id() > 0) {
            return matchService.loadAll(model.getCupId(), model.getTeam2Id());
        }

        return matchService.loadAll(cupService.load((model.getCupId()))); // TODO: paging here
    }

    private List<Match> filterByTeam(final List<Match> matches, final int teamId) {

        return matches
                .stream()
                .filter(filterByTeamPredicate(teamId))
                .collect(Collectors.toList());
    }

    private List<Match> filterOutNotFinished(final List<Match> matches) {

        return matches
                .stream()
                .filter(matchFinishedPredicate())
                .collect(Collectors.toList());
    }

    private List<Match> filterOutFinished(final List<Match> matches) {

        return matches
                .stream()
                .filter(matchFinishedPredicate().negate())
                .collect(Collectors.toList());
    }

    private List<Match> sortByBeginningTimeAsc(final List<Match> matches) {

        return matches
                .stream()
                .sorted(beginningTimeComparator().reversed())
                .collect(Collectors.toList());
    }

    private List<Match> sortByBeginningTimeDesc(List<Match> matches) {
        return matches
                .stream()
                .sorted(beginningTimeComparator())
                .collect(Collectors.toList());
    }

    private Predicate<Match> filterByTeamPredicate(final int teamId) {
        return match -> (match.getTeam1().getId() == teamId) || (match.getTeam2().getId() == teamId);
    }

    private Predicate<Match> matchFinishedPredicate() {
        return matchService::isMatchFinished;
    }

    private Comparator<Match> beginningTimeComparator() {
        return (o1, o2) -> o2.getBeginningTime().compareTo(o1.getBeginningTime());
    }
}
