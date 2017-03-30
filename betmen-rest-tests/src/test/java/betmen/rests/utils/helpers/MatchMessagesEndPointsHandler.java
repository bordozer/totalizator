package betmen.rests.utils.helpers;

import betmen.dto.dto.MatchMessageDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.MatchRoutes;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchMessagesEndPointsHandler {

    public static List<MatchMessageDTO> getMatchMessages(final int matchId) {
        return Arrays.asList(getMatchMessages(matchId, ResponseStatus.OK).as(MatchMessageDTO[].class));
    }

    public static Response getMatchMessages(final int matchId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.MATCH_MESSAGES, ParameterUtils.matchParam(matchId), responseStatus.getCode());
    }

    public static List<MatchMessageDTO> getUserMessages(final int userId) {
        return Arrays.asList(getUserMessages(userId, ResponseStatus.OK).as(MatchMessageDTO[].class));
    }

    public static Response getUserMessages(final int userId, final ResponseStatus responseStatus) {
        return RequestHelper.doGet(MatchRoutes.USER_MESSAGES, ParameterUtils.userParams(userId), responseStatus.getCode());
    }

    public static MatchMessageDTO createMatchMessages(final MatchMessageDTO dto) {
        return createMatchMessages(dto, ResponseStatus.OK).as(MatchMessageDTO.class);
    }

    public static Response createMatchMessages(final MatchMessageDTO dto, final ResponseStatus responseStatus) {
        assertThat("MessageID should be ZERO for a new messages", dto.getId() == 0, is(true));
        return RequestHelper.doJsonPut(MatchRoutes.CREATE_MATCH_MESSAGES, dto, responseStatus.getCode());
    }

    public static MatchMessageDTO updateMatchMessages(final MatchMessageDTO dto) {
        return updateMatchMessages(dto, ResponseStatus.OK).as(MatchMessageDTO.class);
    }

    public static Response updateMatchMessages(final MatchMessageDTO dto, final ResponseStatus responseStatus) {
        assertThat("MessageID should be provided to update message", dto.getId() > 0, is(true));
        return RequestHelper.doJsonPost(MatchRoutes.UPDATE_MATCH_MESSAGES, dto, ParameterUtils.matchParam(dto.getMatchId()), responseStatus.getCode());
    }

    public static Boolean deleteMatchMessages(final int messageId) {
        return deleteMatchMessages(messageId, ResponseStatus.OK).as(Boolean.class);
    }

    public static Response deleteMatchMessages(final int messageId, final ResponseStatus responseStatus) {
        return RequestHelper.doDelete(MatchRoutes.DELETE_MATCH_MESSAGES, ParameterUtils.matchParam(messageId), responseStatus.getCode());
    }
}
