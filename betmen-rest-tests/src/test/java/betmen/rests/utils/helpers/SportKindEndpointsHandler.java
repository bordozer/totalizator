package betmen.rests.utils.helpers;

import betmen.dto.dto.SportKindDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SportKindRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SportKindEndpointsHandler {

    public static List<SportKindDTO> getSports() {
        return Arrays.asList(getSports(ResponseStatus.OK).as(SportKindDTO[].class));
    }

    public static Response getSports(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SportKindRoutes.SPORT_KINDS_LIST, responseStatus.getCode());
    }

    public static SportKindDTO getSport(final int sportKindId) {
        return getSport(sportKindId, ResponseStatus.OK).as(SportKindDTO.class);
    }

    public static Response getSport(final int sportKindId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SportKindRoutes.SPORT_KIND, params(sportKindId), responseStatus.getCode());
    }

    private static Map<String, Object> params(final int sportKindId) {
        return Collections.singletonMap(RestTestConstants.SPORT_KIND_ID, sportKindId);
    }
}
