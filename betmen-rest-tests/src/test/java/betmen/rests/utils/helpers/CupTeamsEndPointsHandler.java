package betmen.rests.utils.helpers;

import betmen.dto.dto.CupTeamsDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.TeamsRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CupTeamsEndPointsHandler {

    public static CupTeamsDTO getCupTeams(final int cupId) {
        return getCupTeams(cupId, ResponseStatus.OK).as(CupTeamsDTO.class);
    }

    public static Response getCupTeams(final int cupId, final ResponseStatus code) {
        return RequestHelper.doGet(TeamsRoutes.CUP_TEAMS, activeCupTeamsParams(cupId, true), code.getCode());
    }

    public static CupTeamsDTO getCupActiveTeams(final int cupId) {
        return getCupTeams(cupId, ResponseStatus.OK).as(CupTeamsDTO.class);
    }

    public static Response getCupActiveTeams(final int cupId, final ResponseStatus code) {
        return RequestHelper.doGet(TeamsRoutes.CUP_TEAMS, activeCupTeamsParams(cupId, true), code.getCode());
    }

    public static CupTeamsDTO getCupByFirstLetter(final int cupId, final String letter) {
        return getCupByFirstLetter(cupId, letter, ResponseStatus.OK).as(CupTeamsDTO.class);
    }

    public static Response getCupByFirstLetter(final int cupId, final String letter, final ResponseStatus code) {
        return RequestHelper.doGet(TeamsRoutes.CUP_TEAMS_STARTED_WITH_LETTER, teamsStartedWithLetterParams(cupId, letter), code.getCode());
    }

    private static Map<String, Object> activeCupTeamsParams(final int cupId, final boolean ative) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.CUP_ID, cupId);
        map.put(RestTestConstants.ONLY_ACTIVE_CUP_TEAMS, ative);
        return map;
    }

    private static Map<String, Object> teamsStartedWithLetterParams(final int cupId, final String letter) {
        Map<String, Object> map = new HashMap<>();
        map.put(RestTestConstants.CUP_ID, cupId);
        map.put(RestTestConstants.TEAM_FIRST_LETTER, letter);
        return map;
    }
}
