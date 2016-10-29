package betmen.rests.utils.helpers;

import betmen.dto.dto.CupDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.CupRoutes;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class CupsEndPointsHandler {

    // Endpoint is deprecated on BE
    public static List<CupDTO> getAllCups() {
        return Arrays.asList(getAllCups(ResponseStatus.OK).as(CupDTO[].class));
    }

    // Endpoint is deprecated on BE
    public static Response getAllCups(final ResponseStatus code) {
        return RequestHelper.doGet(CupRoutes.CUPS, code.getCode());
    }

    public static List<CupDTO> getAllPublicCups() {
        return Arrays.asList(getAllPublicCups(ResponseStatus.OK).as(CupDTO[].class));
    }

    public static Response getAllPublicCups(final ResponseStatus code) {
        return RequestHelper.doGet(CupRoutes.PUBLIC_CUPS, code.getCode());
    }

    public static List<CupDTO> getAllPublicCurrentCups() {
        return Arrays.asList(getAllPublicCurrentCups(ResponseStatus.OK).as(CupDTO[].class));
    }

    public static Response getAllPublicCurrentCups(final ResponseStatus code) {
        return RequestHelper.doGet(CupRoutes.PUBLIC_CURRENT_CUPS, code.getCode());
    }

    public static CupDTO getCup(final int cupId) {
        return getCup(cupId, ResponseStatus.OK).as(CupDTO.class);
    }

    public static Response getCup(final int cupId, final ResponseStatus code) {
        return RequestHelper.doGet(CupRoutes.CUP, ParameterUtils.cupParam(cupId), code.getCode());
    }
}
