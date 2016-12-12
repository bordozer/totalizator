package betmen.rests.utils.helpers;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class ActivityEndPointsHandler {

    public static List<ActivityStreamDTO> getPortalPageActivities() {
        return Arrays.asList(getPortalPageActivities(ResponseStatus.OK).as(ActivityStreamDTO[].class));
    }

    public static Response getPortalPageActivities(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SystemRoutes.PORTAL_PAGE_ACTIVITIES, responseStatus.getCode());
    }

    public static List<ActivityStreamDTO> getMatchPageActivities(final int matchId) {
        return Arrays.asList(getMatchPageActivities(matchId, ResponseStatus.OK).as(ActivityStreamDTO[].class));
    }

    public static Response getMatchPageActivities(final int matchId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SystemRoutes.MATCH_ACTIVITIES, ParameterUtils.matchParam(matchId), responseStatus.getCode());
    }

    public static List<ActivityStreamDTO> getUserPageActivities(final int userId) {
        return Arrays.asList(getUserPageActivities(userId, ResponseStatus.OK).as(ActivityStreamDTO[].class));
    }

    public static Response getUserPageActivities(final int userId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SystemRoutes.USER_ACTIVITIES, ParameterUtils.userParams(userId), responseStatus.getCode());
    }

    public static Response adminActivityStreamCleanup() {
        return RequestHelper.doGet(SystemRoutes.ACTIVITY_STREAM_CLEANUP, ResponseStatus.OK.getCode());
    }
}
