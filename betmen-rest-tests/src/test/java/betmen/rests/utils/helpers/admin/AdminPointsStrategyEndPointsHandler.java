package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.AdminRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminPointsStrategyEndPointsHandler {

    private static final int SC_OK = HttpServletResponse.SC_OK;

    public static List<PointsCalculationStrategyEditDTO> getItems() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.POINTS_CALCULATION_STRATEGIES).as(PointsCalculationStrategyEditDTO[].class));
    }

    public static Response get(final int cupId, final ResponseStatus expectedStatusCode) {
        return RequestHelper.doGet(AdminRoutes.POINTS_CALCULATION_STRATEGY_GET, params(cupId), expectedStatusCode.getCode());
    }

    public static PointsCalculationStrategyEditDTO get(final int cupId) {
        return RequestHelper.doGet(AdminRoutes.POINTS_CALCULATION_STRATEGY_GET, params(cupId), SC_OK).as(PointsCalculationStrategyEditDTO.class);
    }

    public static Response create(final PointsCalculationStrategyEditDTO dto, final int expectedStatusCode) {
        return RequestHelper.doJsonPut(AdminRoutes.POINTS_CALCULATION_STRATEGY_CREATE, dto, expectedStatusCode);
    }

    public static PointsCalculationStrategyEditDTO create(final PointsCalculationStrategyEditDTO dto) {
        return RequestHelper.doJsonPut(AdminRoutes.POINTS_CALCULATION_STRATEGY_CREATE, dto, SC_OK).as(PointsCalculationStrategyEditDTO.class);
    }

    public static PointsCalculationStrategyEditDTO update(final PointsCalculationStrategyEditDTO dto) {
        assertId(dto.getPcsId());
        return RequestHelper.doJsonPut(AdminRoutes.POINTS_CALCULATION_STRATEGY_UPDATE, dto, params(dto.getPcsId())).as(PointsCalculationStrategyEditDTO.class);
    }

    public static Response update(final PointsCalculationStrategyEditDTO dto, final int expectedStatusCode) {
        assertId(dto.getPcsId());
        return RequestHelper.doJsonPut(AdminRoutes.POINTS_CALCULATION_STRATEGY_UPDATE, dto, params(dto.getPcsId()), expectedStatusCode);
    }

    public static Response delete(final int cupId, final int expectedStatusCode) {
        return RequestHelper.doDelete(AdminRoutes.POINTS_CALCULATION_STRATEGY_DELETE, params(cupId), expectedStatusCode);
    }

    public static boolean delete(final int cupId) {
        return RequestHelper.doDelete(AdminRoutes.POINTS_CALCULATION_STRATEGY_DELETE, params(cupId), SC_OK).as(Boolean.class);
    }

    private static void assertId(final int id) {
        Assert.isTrue(id > 0, "ID should be positive");
    }

    private static Map<String, Object> params(final int id) {
        return Collections.singletonMap(RestTestConstants.PCS_ID, id);
    }
}
