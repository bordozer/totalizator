package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.routes.AdminRoutes;
import betmen.rests.utils.ParameterUtils;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTeamEndPointsHandler {

    private static final int SC_OK = HttpServletResponse.SC_OK;

    public static List<TeamEditDTO> getTeamOfCategory(final int categoryId) {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.TEAMS_OF_CATEGORY, ParameterUtils.categoryParams(categoryId), SC_OK).as(TeamEditDTO[].class));
    }

    public static TeamEditDTO get(final int teamId) {
        return RequestHelper.doGet(AdminRoutes.TEAM_GET, teamParams(teamId), SC_OK).as(TeamEditDTO.class);
    }

    public static Response get(final int teamId, final int expectedStatusCode) {
        return RequestHelper.doGet(AdminRoutes.TEAM_GET, teamParams(teamId), expectedStatusCode);
    }

    public static Response create(final int cupId, final TeamEditDTO dto, final int expectedStatusCode) {
        return RequestHelper.doJsonPut(AdminRoutes.TEAM_CREATE, dto, cupParams(cupId), expectedStatusCode);
    }

    public static TeamEditDTO create(final int cupId, final TeamEditDTO dto) {
        return RequestHelper.doJsonPut(AdminRoutes.TEAM_CREATE, dto, cupParams(cupId)).as(TeamEditDTO.class);
    }

    public static TeamEditDTO create(final TeamEditDTO dto) {
        return RequestHelper.doJsonPut(AdminRoutes.TEAM_CREATE, dto, cupParams(0)).as(TeamEditDTO.class);
    }

    public static TeamEditDTO update(final TeamEditDTO dto) {
        assertId(dto.getTeamId());
        return RequestHelper.doJsonPut(AdminRoutes.TEAM_UPDATE, dto, teamParams(dto.getTeamId())).as(TeamEditDTO.class);
    }

    public static Response update(final TeamEditDTO dto, final int expectedStatusCode) {
        assertId(dto.getTeamId());
        return RequestHelper.doJsonPut(AdminRoutes.TEAM_UPDATE, dto, teamParams(dto.getTeamId()), expectedStatusCode);
    }

    public static Response delete(final int teamId, final int expectedStatusCode) {
        return RequestHelper.doDelete(AdminRoutes.TEAM_DELETE, teamParams(teamId), expectedStatusCode);
    }

    public static boolean delete(final int teamId) {
        return RequestHelper.doDelete(AdminRoutes.TEAM_DELETE, teamParams(teamId)).as(Boolean.class);
    }

    public static void activateTeamInCup(final int cupId, final int teamId) {
        setCupTeamActivity(cupId, teamId, true);
    }

    public static void deactivateTeamInCup(final int cupId, final int teamId) {
        setCupTeamActivity(cupId, teamId, false);
    }

    private static void setCupTeamActivity(final int cupId, final int teamId, final boolean active) {
        assertId(cupId);
        assertId(teamId);
        RequestHelper.doJsonPost(AdminRoutes.TEAM_CUP_ACTIVITY, new Object(), teamActivityParams(cupId, teamId, active));
    }

    private static void assertId(final int id) {
        Assert.isTrue(id > 0, "ID should be positive");
    }

    private static Map<String, Object> cupParams(final int cupId) {
        Map<String, Object> params = new HashMap<>();
        params.put(RestTestConstants.CUP_ID, cupId);
        return params;
    }

    private static Map<String, Object> teamParams(final int cupId) {
        Map<String, Object> params = new HashMap<>();
        params.put(RestTestConstants.TEAM_ID, cupId);
        return params;
    }

    private static Map<String, Object> teamActivityParams(final int cupId, final int teamId, final boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put(RestTestConstants.CUP_ID, cupId);
        params.put(RestTestConstants.TEAM_ID, teamId);
        params.put(RestTestConstants.ONLY_ACTIVE_CUP_TEAMS, active);
        return params;
    }
}
