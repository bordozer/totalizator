package betmen.rests.utils.helpers;

import betmen.dto.dto.TeamCardCupData;
import betmen.dto.dto.TeamCardDTO;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.TeamRoutes;
import betmen.rests.utils.RestTestConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TeamEndPointHandler {

    public static TeamCardDTO getTeamCard(final int teamId) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD, params(teamId), ResponseStatus.OK.getCode()).as(TeamCardDTO.class);
    }

    public static CommonErrorResponse getTeamCard(final int teamId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD, params(teamId), responseStatus.getCode()).as(CommonErrorResponse.class);
    }

    public static TeamCardCupData getTeamCupStatistics(final int teamId, final int cupId) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD_STATISTICS, paramsStatistics(teamId, cupId), ResponseStatus.OK.getCode()).as(TeamCardCupData.class);
    }

    public static CommonErrorResponse getTeamCupStatistics(final int teamId, final int cupId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(TeamRoutes.TEAM_CARD_STATISTICS, paramsStatistics(teamId, cupId), responseStatus.getCode()).as(CommonErrorResponse.class);
    }

    private static Map<String, Object> params(final int teamId) {
        return Collections.singletonMap(RestTestConstants.TEAM_ID, teamId);
    }

    private static Map<String, Object> paramsStatistics(final int teamId, final int cupId) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.TEAM_ID, teamId);
        map.put(RestTestConstants.CUP_ID, cupId);
        return map;
    }
}
