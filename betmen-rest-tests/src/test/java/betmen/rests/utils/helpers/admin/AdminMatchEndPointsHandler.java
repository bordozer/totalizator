package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.AdminRoutes;
import betmen.rests.utils.helpers.MatchEndPointsHandler;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

public class AdminMatchEndPointsHandler {

    private static final int SC_OK = ResponseStatus.OK.getCode();
    private static final String ID_SHOULD_BE_POSITIVE = "ID should be positive";
    private static final String ID_SHOULD_BE_ZERO = "ID should be zero";

    public static List<MatchEditDTO> getItems(final MatchSearchModelDto searchModel) {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.MATCHES_AND_BETS_WIDGET, MatchEndPointsHandler.searchParams(searchModel), SC_OK).as(MatchEditDTO[].class));
    }

    public static MatchEditDTO get(final int matchId) {
        return RequestHelper.doGet(AdminRoutes.MATCH_GET, ParameterUtils.matchParam(matchId), SC_OK).as(MatchEditDTO.class);
    }

    public static Response get(final int matchId, final int expectedStatusCode) {
        return RequestHelper.doGet(AdminRoutes.MATCH_GET, ParameterUtils.matchParam(matchId), expectedStatusCode);
    }

    public static Response create(final MatchEditDTO dto, final int expectedStatusCode) {
        Assert.isTrue(dto.getMatchId() == 0, ID_SHOULD_BE_ZERO);
        return RequestHelper.doJsonPut(AdminRoutes.MATCH_CREATE, dto, expectedStatusCode);
    }

    public static MatchEditDTO create(final MatchEditDTO dto) {
        Assert.isTrue(dto.getMatchId() == 0, ID_SHOULD_BE_ZERO);
        return RequestHelper.doJsonPut(AdminRoutes.MATCH_CREATE, dto).as(MatchEditDTO.class);
    }

    public static MatchEditDTO update(final MatchEditDTO dto) {
        Assert.isTrue(dto.getMatchId() > 0, ID_SHOULD_BE_POSITIVE);
        return RequestHelper.doJsonPut(AdminRoutes.MATCH_UPDATE, dto, ParameterUtils.matchParam(dto.getMatchId())).as(MatchEditDTO.class);
    }

    public static Response update(final MatchEditDTO dto, final int expectedStatusCode) {
        Assert.isTrue(dto.getMatchId() > 0, ID_SHOULD_BE_POSITIVE);
        return RequestHelper.doJsonPut(AdminRoutes.MATCH_UPDATE, dto, ParameterUtils.matchParam(dto.getMatchId()), expectedStatusCode);
    }

    public static Response delete(final int matchId, final int expectedStatusCode) {
        return RequestHelper.doDelete(AdminRoutes.MATCH_DELETE, ParameterUtils.matchParam(matchId), expectedStatusCode);
    }

    public static boolean delete(final int matchId) {
        return RequestHelper.doDelete(AdminRoutes.MATCH_DELETE, ParameterUtils.matchParam(matchId)).as(Boolean.class);
    }

}
