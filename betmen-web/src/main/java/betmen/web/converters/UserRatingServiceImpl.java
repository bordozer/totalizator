package betmen.web.converters;

import betmen.core.service.points.MatchPointsService;
import betmen.core.model.points.UserSummaryPointsHolder;
import betmen.core.utils.Constants;
import betmen.dto.dto.points.scores.CupUsersScoresInTimeDTO;
import betmen.dto.dto.points.scores.UserMatchesPointsDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

@Service
public class UserRatingServiceImpl implements UserRatingService {

    private static final int BARCHART_IN_TIME_DAYS = 7;

    @Autowired
    private UserService userService;

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private MatchPointsService matchPointsService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private DTOService dtoService;

    @Override
    @Cacheable(value = Constants.USER_RATING_SERVICE_CACHE_QUERY)
    public CupUsersScoresInTimeDTO cupUsersScoresInTime(final int cupId, final int userGroupId) {

        final Cup cup = cupService.load(cupId);

        final List<Match> matches = matchService.loadAllFinished(cup.getId());

        if (matches == null || matches.size() == 0) {
            return new CupUsersScoresInTimeDTO(newArrayList(), newHashMap());
        }

        final List<User> users = userGroupId != 0 ? userGroupService.loadUserGroupMembers(userGroupService.load(userGroupId)) : userService.loadAll();
        final List<LocalDate> dates = getBarchartDates(matches);

        if (dates.size() == 0) {
            return new CupUsersScoresInTimeDTO(newArrayList(), newHashMap());
        }

        final LocalDate startDate = matchService.getFirstMatch(cup).getBeginningTime().toLocalDate();

        final Map<Integer, UserMatchesPointsDTO> map = newLinkedHashMap();

        users
                .stream()
                .forEach(new Consumer<User>() {
                    @Override
                    public void accept(final User user) {

                        final UserDTO userDTO = dtoService.transformUser(user);

                        final UserSummaryPointsHolder summaryPointsInThePeriod = matchPointsService.getUserRating(user, cup, dates.get(0), dates.get(dates.size() - 1));
                        if (summaryPointsInThePeriod == null) {
                            return;
                        }

                        final List<UserMatchPointsHolderDTO> list = newArrayList();

                        dates
                                .stream()
                                .forEach(new Consumer<LocalDate>() {
                                    @Override
                                    public void accept(final LocalDate date) {

                                        final UserSummaryPointsHolder usersRatingOnDate = matchPointsService.getUserRating(user, cup, startDate, date);

                                        if (usersRatingOnDate == null) {
                                            list.add(new UserMatchPointsHolderDTO(userDTO, 0, 0));
                                            return;
                                        }

                                        list.add(new UserMatchPointsHolderDTO(userDTO, usersRatingOnDate.getBetPoints(), usersRatingOnDate.getMatchBonus()));
                                    }
                                });

                        if (list.size() > 0) {
                            map.put(user.getId(), new UserMatchesPointsDTO(userDTO, list));
                        }
                    }
                });

        final List<String> datesString = dates
                .stream()
                .map(new Function<LocalDate, String>() {
                    @Override
                    public String apply(final LocalDate date) {
                        return dateTimeService.formatDate(date);
                    }
                })
                .collect(Collectors.toList());

        final Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {

            final int userId = iterator.next();

            final List<UserMatchPointsHolderDTO> userMatchPointsHolderDTOs = map.get(userId).getUserMatchesPoints();

            final Double summaryPoints = userMatchPointsHolderDTOs
                    .stream()
                    .collect(Collectors.summingDouble(UserMatchPointsHolderDTO::getSummary));

            if (summaryPoints == 0) {
                iterator.remove();
            }
        }

        return new CupUsersScoresInTimeDTO(datesString, map);

    }

    private List<LocalDate> getBarchartDates(final List<Match> matches) {

        final List<LocalDate> dates = matches
                .stream()
                .map(new Function<Match, LocalDate>() {
                    @Override
                    public LocalDate apply(final Match match) {
                        return match.getBeginningTime().toLocalDate();
                    }
                })
                .distinct()
                .sorted(new Comparator<LocalDate>() {
                    @Override
                    public int compare(final LocalDate o1, final LocalDate o2) {
                        return o1.compareTo(o2);
                    }
                })
                .collect(Collectors.toList());

        if (dates.size() <= BARCHART_IN_TIME_DAYS) {
            return dates;
        }

        return dates.subList(dates.size() - BARCHART_IN_TIME_DAYS, dates.size());
    }
}
