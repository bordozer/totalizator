package betmen.rests.utils.helpers;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.PointsCalculationStrategyDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.PointsCalculationStrategyRoute;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PointsCalculationStrategyEndpointHandler {

    public static List<PointsCalculationStrategyDTO> getPointsCalculationStrategies() {
        return Arrays.asList(getPointsCalculationStrategies(ResponseStatus.OK).as(PointsCalculationStrategyDTO[].class));
    }

    public static Response getPointsCalculationStrategies(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(PointsCalculationStrategyRoute.POINTS_CALCULATION_STRATEGY_LIST, responseStatus.getCode());
    }

    public static List<CupDTO> getCupsOf(final int pcsId) {
        return Arrays.asList(getCupsOf(pcsId, ResponseStatus.OK).as(CupDTO[].class));
    }

    public static Response getCupsOf(final int pcsId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(PointsCalculationStrategyRoute.POINTS_CALCULATION_STRATEGY, params(pcsId), responseStatus.getCode());
    }

    private static Map<String, Object> params(final int pcsId) {
        return Collections.singletonMap(RestTestConstants.PCS_ID, pcsId);
    }
}
