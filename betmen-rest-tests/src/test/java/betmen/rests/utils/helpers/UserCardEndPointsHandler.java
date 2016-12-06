package betmen.rests.utils.helpers;

import betmen.dto.dto.UserCardDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.UserRoutes;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserCardEndPointsHandler {

    public static UserCardDTO getUserCardOnDate(final int userId, final LocalDate onDate) {
        return getUserCardOnDate(userId, onDate, ResponseStatus.OK).as(UserCardDTO.class);
    }

    public static Response getUserCardOnDate(final int userId, final LocalDate onDate, final ResponseStatus expectedCode) {
        return RequestHelper.doGet(UserRoutes.USER_CARD, userCardOnDateParams(userId, onDate), expectedCode.getCode());
    }

    public static UserCardDTO getUserCardOnDateFilterByCup(final int userId, final LocalDate onDate, final int cupId) {
        return getUserCardOnDateFilterByCup(userId, onDate, cupId, ResponseStatus.OK).as(UserCardDTO.class);
    }

    public static Response getUserCardOnDateFilterByCup(final int userId, final LocalDate onDate, final int cupId, final ResponseStatus expectedCode) {
        return RequestHelper.doGet(UserRoutes.USER_CARD, userCardOnDateFilterByCupParams(userId, onDate, cupId), expectedCode.getCode());
    }

    private static Map<String, Object> userCardOnDateParams(final int userId, final LocalDate date) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.USER_ID, userId);
        map.put(RestTestConstants.ON_DATE, convertDate(date));
        return map;
    }

    private static Map<String, Object> userCardOnDateFilterByCupParams(final int userId, final LocalDate date, final int cupId) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.USER_ID, userId);
        map.put(RestTestConstants.ON_DATE, convertDate(date));
        map.put(RestTestConstants.CUP_ID, cupId);
        return map;
    }

    private static String convertDate(final LocalDate date) {
        return date != null ? DateTimeUtils.formatDate(date) : "";
    }
}
