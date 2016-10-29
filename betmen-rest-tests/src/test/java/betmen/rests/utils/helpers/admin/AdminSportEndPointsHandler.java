package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.admin.SportKindEditDTO;
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

public class AdminSportEndPointsHandler {

    public static SportKindEditDTO get(final int sportKindId) {
        return RequestHelper.doGet(AdminRoutes.SPORT_KIND_GET, params(sportKindId), HttpServletResponse.SC_OK).as(SportKindEditDTO.class);
    }

    public static Response get(final int sportKindId, final ResponseStatus expectedStatusCode) {
        return RequestHelper.doGet(AdminRoutes.SPORT_KIND_GET, params(sportKindId), expectedStatusCode.getCode());
    }

    public static List<SportKindEditDTO> getItems() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.SPORT_KINDS).as(SportKindEditDTO[].class));
    }

    public static SportKindEditDTO create(final SportKindEditDTO dto) {
        return RequestHelper.doJsonPut(AdminRoutes.SPORT_KIND_CREATE, dto).as(SportKindEditDTO.class);
    }

    public static Response create(final SportKindEditDTO dto, final int expectedStatusCode) {
        return RequestHelper.doJsonPut(AdminRoutes.SPORT_KIND_CREATE, dto, expectedStatusCode);
    }

    public static SportKindEditDTO update(final SportKindEditDTO dto) {
        assertId(dto.getSportKindId());
        return RequestHelper.doJsonPut(AdminRoutes.SPORT_KIND_UPDATE, dto, params(dto.getSportKindId())).as(SportKindEditDTO.class);
    }

    public static Response update(final SportKindEditDTO dto, final int expectedStatusCode) {
        assertId(dto.getSportKindId());
        return RequestHelper.doJsonPut(AdminRoutes.SPORT_KIND_UPDATE, dto, params(dto.getSportKindId()), expectedStatusCode);
    }

    public static Response delete(final int sportKindId, final int expectedStatusCode) {
        return RequestHelper.doDelete(AdminRoutes.SPORT_KIND_DELETE, params(sportKindId), expectedStatusCode);
    }

    public static boolean delete(final int sportKindId) {
        return RequestHelper.doDelete(AdminRoutes.SPORT_KIND_DELETE, params(sportKindId), HttpServletResponse.SC_OK).as(Boolean.class);
    }

    private static Map<String, Object> params(final int id) {
        return Collections.singletonMap(RestTestConstants.SPORT_KIND_ID, id);
    }

    private static void assertId(final int id) {
        Assert.isTrue(id > 0, "ID should be positive");
    }
}
