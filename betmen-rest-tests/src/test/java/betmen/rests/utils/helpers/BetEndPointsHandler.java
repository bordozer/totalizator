package betmen.rests.utils.helpers;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.MatchRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BetEndPointsHandler {

    private static final int SC_OK = HttpServletResponse.SC_OK;
    private static final String EMPTY_JSON_BODY = StringUtils.EMPTY;

    public static Response get(final int matchId, final int userId, final int expectedStatusCode) {
        return RequestHelper.doGet(MatchRoutes.GET_MATCH_BET, userMatchBetParams(matchId, userId), expectedStatusCode);
    }

    public static MatchBetDTO get(final int matchId, final int userId) {
        return RequestHelper.doGet(MatchRoutes.GET_MATCH_BET, userMatchBetParams(matchId, userId), SC_OK).as(MatchBetDTO.class);
    }

    public static Response make(final int matchId, final int score1, final int score2, final ResponseStatus expectedStatusCode) {
        return RequestHelper.doJsonPost(MatchRoutes.MAKE_MATCH_BET, EMPTY_JSON_BODY, makeBetParams(matchId, score1, score2), expectedStatusCode.getCode());
    }

    public static BetDTO make(final int matchId, final int score1, final int score2) {
        return RequestHelper.doJsonPost(MatchRoutes.MAKE_MATCH_BET, EMPTY_JSON_BODY, makeBetParams(matchId, score1, score2), SC_OK).as(BetDTO.class);
    }

    public static Response delete(final int matchId, final int expectedStatusCode) {
        return RequestHelper.doDelete(MatchRoutes.DELETE_MATCH_BET, matchParam(matchId), expectedStatusCode);
    }

    public static boolean delete(final int matchId) {
        return RequestHelper.doDelete(MatchRoutes.DELETE_MATCH_BET, matchParam(matchId), SC_OK).as(Boolean.class);
    }

    private static Map<String, Object> userMatchBetParams(final int matchId, final int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.MATCH_ID, matchId);
        map.put(RestTestConstants.USER_ID, userId);
        return map;
    }

    private static Map<String, Object> makeBetParams(final int matchId, final int score1, final int score2) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.MATCH_ID, matchId);
        map.put(RestTestConstants.SCORE1, score1);
        map.put(RestTestConstants.SCORE2, score2);
        return map;
    }

    private static Map<String, Object> matchParam(final int matchBetId) {
        return Collections.singletonMap(RestTestConstants.MATCH_ID, matchBetId);
    }
}
