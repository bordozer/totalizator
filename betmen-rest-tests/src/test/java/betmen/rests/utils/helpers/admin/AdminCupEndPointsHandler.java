package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupForGameImportDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.AdminRoutes;
import betmen.rests.utils.RestTestConstants;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminCupEndPointsHandler {

    private static final int SC_OK = ResponseStatus.OK.getCode();

    public static List<CupEditDTO> getCups() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_EDIT).as(CupEditDTO[].class));
    }

    public static CupDTO getCup(final int cupId) {
        return RequestHelper.doGet(AdminRoutes.CUP, ParameterUtils.cupParam(cupId), SC_OK).as(CupDTO.class);
    }

    public static CupEditDTO getEdit(final int cupId) {
        return RequestHelper.doGet(AdminRoutes.CUP_EDIT_GET, ParameterUtils.cupParam(cupId), SC_OK).as(CupEditDTO.class);
    }

    public static Response getEdit(final int cupId, final ResponseStatus expectedResponseStatus) {
        return RequestHelper.doGet(AdminRoutes.CUP_EDIT_GET, ParameterUtils.cupParam(cupId), expectedResponseStatus.getCode());
    }

    public static List<CupDTO> getAllCups() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_ALL, Collections.emptyMap(), SC_OK).as(CupDTO[].class));
    }

    public static List<CupDTO> getAllCurrentCups() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_ALL_CURRENT, Collections.emptyMap(), SC_OK).as(CupDTO[].class));
    }

    public static List<CupForGameImportDTO> getAllImportableCups(final int sportId) {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_ALL_IMPORTABLE, Collections.singletonMap(RestTestConstants.SPORT_KIND_ID, sportId), SC_OK).as(CupForGameImportDTO[].class));
    }

    public static List<CupDTO> getAllCurrentImportableCups(final int sportId) {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_ALL_CURRENT_IMPORTABLE, Collections.singletonMap(RestTestConstants.SPORT_KIND_ID, sportId), SC_OK).as(CupDTO[].class));
    }

    public static List<CupDTO> getAllCategoryCups(final int categoryId) {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CUPS_OF_CATEGORY, Collections.singletonMap(RestTestConstants.CATEGORY_ID, categoryId), SC_OK).as(CupDTO[].class));
    }

    public static Response create(final CupEditDTO dto, final ResponseStatus expectedResponseStatus) {
        return RequestHelper.doJsonPut(AdminRoutes.CUP_CREATE, dto, expectedResponseStatus.getCode());
    }

    public static CupEditDTO create(final CupEditDTO dto) {
        return RequestHelper.doJsonPut(AdminRoutes.CUP_CREATE, dto, SC_OK).as(CupEditDTO.class);
    }

    public static CupEditDTO update(final CupEditDTO dto) {
        Assert.isTrue(dto.getCupId() > 0, "ID should be positive");
        return RequestHelper.doJsonPut(AdminRoutes.CUP_UPDATE, dto, ParameterUtils.cupParam(dto.getCupId())).as(CupEditDTO.class);
    }

    public static Response update(final CupEditDTO dto, final ResponseStatus expectedResponseStatus) {
        Assert.isTrue(dto.getCupId() > 0, "ID should be positive");
        return RequestHelper.doJsonPut(AdminRoutes.CUP_UPDATE, dto, ParameterUtils.cupParam(dto.getCupId()), expectedResponseStatus.getCode());
    }

    public static Response delete(final int cupId, final ResponseStatus expectedResponseStatus) {
        return RequestHelper.doDelete(AdminRoutes.CUP_DELETE, ParameterUtils.cupParam(cupId), expectedResponseStatus.getCode());
    }

    public static boolean delete(final int cupId) {
        return RequestHelper.doDelete(AdminRoutes.CUP_DELETE, ParameterUtils.cupParam(cupId), SC_OK).as(Boolean.class);
    }

}
