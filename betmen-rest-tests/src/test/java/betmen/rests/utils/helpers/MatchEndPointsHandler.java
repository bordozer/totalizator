package betmen.rests.utils.helpers;

import betmen.dto.dto.MatchBetsOnDateDTO;
import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.MatchRoutes;
import betmen.rests.utils.ParameterUtils;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchEndPointsHandler {

    public static List<MatchBetsOnDateDTO> searchMatches(final MatchSearchModelDto searchModel) {
        return Arrays.asList(RequestHelper.doGet(MatchRoutes.MATCHES_AND_BETS_WIDGET, searchParams(searchModel), ResponseStatus.OK.getCode()).as(MatchBetsOnDateDTO[].class));
    }

    public static Response searchMatches(final MatchSearchModelDto searchModel, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.MATCHES_AND_BETS_WIDGET, searchParams(searchModel), responseStatus.getCode());
    }

    public static MatchDTO getMatch(final int matchId) {
        return getMatch(matchId, ResponseStatus.OK).as(MatchDTO.class);
    }

    public static Response getMatch(final int matchId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.GET_MATCH, ParameterUtils.matchParam(matchId), responseStatus.getCode());
    }

    public static List<MatchDTO> getCupMatches(final int cupId) {
        return Arrays.asList(getCupMatches(cupId, ResponseStatus.OK).as(MatchDTO[].class));
    }

    public static Response getCupMatches(final int cupId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.GET_CUP_MATCHES, ParameterUtils.cupParam(cupId), responseStatus.getCode());
    }

    public static List<MatchDTO> getCupTeamsMatches(final int cupId, final int team1, final int team2) {
        return Arrays.asList(getCupTeamsMatches(cupId, team1, team2, ResponseStatus.OK).as(MatchDTO[].class));
    }

    public static Response getCupTeamsMatches(final int cupId, final int team1, final int team2, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.GET_CUP_TEAMS_MATCHES, cupTeamsParams(cupId, team1, team2), responseStatus.getCode());
    }

    public static List<MatchDTO> getCupTeamsMatchesFinished(final int cupId, final int team1, final int team2) {
        return Arrays.asList(getCupTeamsMatchesFinished(cupId, team1, team2, ResponseStatus.OK).as(MatchDTO[].class));
    }

    public static Response getCupTeamsMatchesFinished(final int cupId, final int team1, final int team2, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.GET_CUP_TEAMS_MATCHES_FINISHED, cupTeamsParams(cupId, team1, team2), responseStatus.getCode());
    }

    public static Map<String, Object> searchParams(final MatchSearchModelDto searchModel) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.CATEGORY_ID, searchModel.getCategoryId());
        map.put(RestTestConstants.CUP_ID, searchModel.getCupId());
        map.put(RestTestConstants.MNB_SHOW_FUTURE_MATCHES, searchModel.isShowFutureMatches());
        map.put(RestTestConstants.MNB_SHOW_FINISHED, searchModel.isShowFinished());
        map.put(RestTestConstants.MNB_FILTER_BY_DATE_ENABLE, searchModel.isFilterByDateEnable());
        map.put(RestTestConstants.MNB_FILTER_BY_DATE, searchModel.getFilterByDate());
        map.put(RestTestConstants.MNB_SORTING, searchModel.getSorting());
        map.put(RestTestConstants.USER_ID, searchModel.getUserId());
        map.put(RestTestConstants.TEAM_ID, searchModel.getTeamId());
        map.put(RestTestConstants.MNB_TEAM_2_ID, searchModel.getTeam2Id());
        return map;
    }

    private static Map<String, Object> cupTeamsParams(final int cupId, final int team1, final int team2) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.CUP_ID, cupId);
        map.put("team1Id", team1);
        map.put("team2Id", team2);
        return map;
    }
}
