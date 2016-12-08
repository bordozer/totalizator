package betmen.rests.utils.helpers;

import betmen.dto.dto.TeamCardCupData;
import betmen.dto.dto.TeamCardDTO;
import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.TeamRoutes;
import betmen.rests.utils.ParameterUtils;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamEndPointHandler {

    public static TeamCardDTO getTeamCard(final int teamId) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD, ParameterUtils.teamParams(teamId), ResponseStatus.OK.getCode()).as(TeamCardDTO.class);
    }

    public static CommonErrorResponse getTeamCard(final int teamId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD, ParameterUtils.teamParams(teamId), responseStatus.getCode()).as(CommonErrorResponse.class);
    }

    public static TeamCardCupData getTeamCupStatistics(final int teamId, final int cupId) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD_STATISTICS, paramsStatistics(teamId, cupId), ResponseStatus.OK.getCode()).as(TeamCardCupData.class);
    }

    public static CommonErrorResponse getTeamCupStatistics(final int teamId, final int cupId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD_STATISTICS, paramsStatistics(teamId, cupId), responseStatus.getCode()).as(CommonErrorResponse.class);
    }

    public static TeamDTO getTeam(final int teamId) {
        return getTeam(teamId, ResponseStatus.OK).as(TeamDTO.class);
    }

    public static Response getTeam(final int teamId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.GET_TEAM, ParameterUtils.teamParams(teamId), responseStatus.getCode());
    }

    public static List<TeamDTO> getAllCategoryTeams(final int categoryId) {
        return Arrays.asList(getAllCategoryTeams(categoryId, ResponseStatus.OK).as(TeamDTO[].class));
    }

    public static Response getAllCategoryTeams(final int categoryId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.GET_ALL_CATEGORY_TEAMS, ParameterUtils.categoryParams(categoryId), responseStatus.getCode());
    }

    public static List<TeamDTO> getAllCupTeams(final int cupId) {
        return Arrays.asList(getAllCupTeams(cupId, ResponseStatus.OK).as(TeamDTO[].class));
    }

    public static Response getAllCupTeams(final int cupId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.GET_ALL_CUP_TEAMS, ParameterUtils.cupParam(cupId), responseStatus.getCode());
    }

    public static List<TeamDTO> getAllCupActiveTeams(final int cupId) {
        return Arrays.asList(getAllCupActiveTeams(cupId, ResponseStatus.OK).as(TeamDTO[].class));
    }

    public static Response getAllCupActiveTeams(final int cupId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.GET_ALL_CUP_ACTIVE_TEAMS, ParameterUtils.cupParam(cupId), responseStatus.getCode());
    }

    private static Map<String, Object> paramsStatistics(final int teamId, final int cupId) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.TEAM_ID, teamId);
        map.put(RestTestConstants.CUP_ID, cupId);
        return map;
    }
}
