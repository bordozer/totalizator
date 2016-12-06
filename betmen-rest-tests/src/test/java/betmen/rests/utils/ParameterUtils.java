package betmen.rests.utils;

import java.util.Collections;
import java.util.Map;

public class ParameterUtils {

    public static Map<String, Object> userParams(final int userId) {
        return Collections.singletonMap(RestTestConstants.USER_ID, userId);
    }

    public static Map<String, Object> cupParam(final int cupId) {
        return Collections.singletonMap(RestTestConstants.CUP_ID, cupId);
    }

    public static Map<String, Object> matchParam(final int matchId) {
        return Collections.singletonMap(RestTestConstants.MATCH_ID, matchId);
    }

    public static Map<String, Object> categoryParams(final int id) {
        return Collections.singletonMap(RestTestConstants.CATEGORY_ID, id);
    }
}
