package betmen.rests.utils.helpers;

import betmen.dto.dto.points.scores.CupUsersScoresDTO;
import betmen.dto.dto.points.scores.CupUsersScoresInTimeDTO;
import betmen.dto.dto.UsersRatingPositionDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.UserPointsRoutes;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RestTestConstants;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersPointsEndpointHandler {
    private static final int SC_OK = ResponseStatus.OK.getCode();

    public static CupUsersScoresDTO getCupScores(final int cupId) {
        return RequestHelper.doGet(UserPointsRoutes.CUP_POINTS, cupAndUserGroupParams(cupId, 0), SC_OK).as(CupUsersScoresDTO.class);
    }

    public static CupUsersScoresDTO getCupScores(final int cupId, final int userGroupId) {
        return RequestHelper.doGet(UserPointsRoutes.CUP_POINTS, cupAndUserGroupParams(cupId, userGroupId), SC_OK).as(CupUsersScoresDTO.class);
    }

    public static CupUsersScoresInTimeDTO getCupScoresInTime(final int cupId) {
        return RequestHelper.doGet(UserPointsRoutes.CUP_POINTS_IN_TIME, cupAndUserGroupParams(cupId, 0), SC_OK).as(CupUsersScoresInTimeDTO.class);
    }

    public static CupUsersScoresInTimeDTO getCupScoresInTime(final int cupId, final int userGroupId) {
        return RequestHelper.doGet(UserPointsRoutes.CUP_POINTS_IN_TIME, cupAndUserGroupParams(cupId, userGroupId), SC_OK).as(CupUsersScoresInTimeDTO.class);
    }

    public static List<UsersRatingPositionDTO> getUsersRating(final LocalDate dateFrom, final LocalDate dateTo) {
        return Arrays.asList(RequestHelper.doGet(UserPointsRoutes.USERS_RATING, dateRangeParameters(dateFrom, dateTo), SC_OK).as(UsersRatingPositionDTO[].class));
    }

    private static Map<String, Object> cupParams(final int cupId) {
        return Collections.singletonMap(RestTestConstants.CUP_ID, cupId);
    }

    private static Map<String, Object> cupAndUserGroupParams(final int cupId, final int userGroupId) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.CUP_ID, cupId);
        map.put(RestTestConstants.USER_GROUP_ID, userGroupId);
        return map;
    }

    private static Map<String, Object> dateRangeParameters(final LocalDate dateFrom, final LocalDate dateTo) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.DATE_FROM, DateTimeUtils.formatDate(dateFrom));
        map.put(RestTestConstants.DATE_TO, DateTimeUtils.formatDate(dateTo));
        return map;
    }
}
