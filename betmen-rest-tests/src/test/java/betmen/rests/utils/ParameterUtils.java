package betmen.rests.utils;

import betmen.rests.utils.RestTestConstants;

import java.util.Collections;
import java.util.Map;

public class ParameterUtils {

    public static Map<String, Object> cupParam(final int cupId) {
        return Collections.singletonMap(RestTestConstants.CUP_ID, cupId);
    }

    public static Map<String, Object> matchParam(final int matchId) {
        return Collections.singletonMap(RestTestConstants.MATCH_ID, matchId);
    }
}