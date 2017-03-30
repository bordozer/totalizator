package betmen.rests.tests;

import betmen.dto.dto.MatchMessageDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.RestTestUser;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.MatchMessagesEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class MatchMessagesRestTest {

    @BeforeMethod
    public void beforeEachMethod() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToDealWithMessagesForAnonymousUser() {
        MatchMessagesEndPointsHandler.getMatchMessages(0, ResponseStatus.UNAUTHORIZED);
        MatchMessagesEndPointsHandler.getUserMessages(0, ResponseStatus.UNAUTHORIZED);
        MatchMessagesEndPointsHandler.createMatchMessages(new MatchMessageDTO(), ResponseStatus.UNAUTHORIZED);

        MatchMessageDTO dto = new MatchMessageDTO();
        dto.setId(345);
        MatchMessagesEndPointsHandler.updateMatchMessages(dto, ResponseStatus.UNAUTHORIZED);

        MatchMessagesEndPointsHandler.deleteMatchMessages(2345, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturn422IfRequestedNotExistingMatch() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        List<MatchMessageDTO> matchMessages = MatchMessagesEndPointsHandler.getMatchMessages(0);
        assertThat(matchMessages, notNullValue());
        assertThat(matchMessages, hasSize(0));
    }

    @Test
    public void shouldReturnEmptyListIfNoMessageForMatch() {
        AuthEndPointsHandler.loginAsAdmin();
        MatchEditDTO matchEdit = AdminTestDataGenerator.createRandomMatch();

        List<MatchMessageDTO> matchMessages = MatchMessagesEndPointsHandler.getMatchMessages(matchEdit.getMatchId());
        assertThat(matchMessages, notNullValue());
        assertThat(matchMessages, hasSize(0));
    }

    @Test
    public void shouldCreateMatchMessageAndUpdateIt() {
        AuthEndPointsHandler.loginAsAdmin();
        int matchId = AdminTestDataGenerator.createRandomMatch().getMatchId();

        MatchMessageDTO dto = new MatchMessageDTO();
        dto.setMatchId(matchId);
        dto.setMessageText("Match comment");

        // create a message
        MatchMessageDTO createdMatchMessage = MatchMessagesEndPointsHandler.createMatchMessages(dto);
        assertThat(createdMatchMessage, notNullValue());
        assertThat(createdMatchMessage.getId() > 0, is(true));
        assertThat(createdMatchMessage.getMatchId(), is(matchId));
        assertThat(createdMatchMessage.getMessageTime(), notNullValue());
        assertThat(createdMatchMessage.getMessageText(), is("Match comment"));

        List<MatchMessageDTO> matchMessages = MatchMessagesEndPointsHandler.getMatchMessages(matchId);
        assertThat(matchMessages, notNullValue());
        assertThat(matchMessages, hasSize(1));

        MatchMessageDTO messageDTO = matchMessages.get(0);
        assertThat(messageDTO.getId(), is(createdMatchMessage.getId()));
        assertThat(messageDTO.getMatchId(), is(matchId));
        assertThat(messageDTO.getMessageTime(), is(createdMatchMessage.getMessageTime()));
        assertThat(messageDTO.getMessageText(), is("Match comment"));

        // update the message
        createdMatchMessage.setMessageTime(LocalDateTime.of(2017, 1, 30, 15, 12));
        createdMatchMessage.setMessageText("Updated message");
        MatchMessageDTO updatedMatchMessage = MatchMessagesEndPointsHandler.updateMatchMessages(createdMatchMessage);
        assertThat(updatedMatchMessage, notNullValue());
        assertThat(updatedMatchMessage.getId(), is(createdMatchMessage.getId()));
        assertThat(updatedMatchMessage.getMatchId(), is(matchId));
        assertThat(updatedMatchMessage.getMessageTime(), notNullValue());
        assertThat(updatedMatchMessage.getMessageText(), is("Updated message"));

        // get messages again
        List<MatchMessageDTO> matchMessages1 = MatchMessagesEndPointsHandler.getMatchMessages(matchId);
        assertThat(matchMessages1, hasSize(1));
        assertThat(matchMessages1.get(0).getId(), is(createdMatchMessage.getId()));
        assertThat(matchMessages1.get(0).getMessageText(), is("Updated message"));
    }

    @Test
    public void shouldReadUserMatchMessages() {
        RestTestUser admin = AuthEndPointsHandler.loginAsAdmin();
        int matchId = AdminTestDataGenerator.createRandomMatch().getMatchId();

        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        List<MatchMessageDTO> userMatchMessagesForUser = MatchMessagesEndPointsHandler.getUserMessages(user.getUserId());
        assertThat(userMatchMessagesForUser, hasSize(0));

        MatchMessageDTO userComment = new MatchMessageDTO();
        userComment.setMatchId(matchId);
        userComment.setMessageText("User comment");
        MatchMessagesEndPointsHandler.createMatchMessages(userComment);

        List<MatchMessageDTO> userMatchMessagesForUser2 = MatchMessagesEndPointsHandler.getUserMessages(user.getUserId());
        assertThat(userMatchMessagesForUser2, hasSize(1));
        assertThat(userMatchMessagesForUser2.get(0).getMessageText(), is("User comment"));

        AuthEndPointsHandler.loginAsAdmin();
        List<MatchMessageDTO> userMatchMessagesForAdmin = MatchMessagesEndPointsHandler.getUserMessages(user.getUserId());
        assertThat(userMatchMessagesForAdmin, hasSize(1));
        assertThat(userMatchMessagesForAdmin.get(0).getMessageText(), is("User comment"));
    }

    @Test
    public void shouldNotUpdateMessageOfAnotherUser() {
        AuthEndPointsHandler.loginAsAdmin();
        int matchId = AdminTestDataGenerator.createRandomMatch().getMatchId();

        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        MatchMessageDTO userComment = new MatchMessageDTO();
        userComment.setMatchId(matchId);
        userComment.setMessageText("User comment");
        MatchMessageDTO userMatchMessages = MatchMessagesEndPointsHandler.createMatchMessages(userComment);

        AuthEndPointsHandler.loginAsAdmin();
        userMatchMessages.setMessageText("Message changed by admin");
        MatchMessagesEndPointsHandler.updateMatchMessages(userMatchMessages, ResponseStatus.UNPROCESSABLE_ENTITY);

        List<MatchMessageDTO> messages = MatchMessagesEndPointsHandler.getUserMessages(user.getUserId());
        assertThat(messages.get(0).getId(), is(userMatchMessages.getId()));
        assertThat(messages.get(0).getMessageText(), is("User comment"));
    }

    @Test
    public void shouldNotAllowToCreateMessageIfNoMatchIdOrText() {
        UserDTO user = AuthEndPointsHandler.registerNewUserAndLogin();

        FieldErrorsResponse errorsResponse = MatchMessagesEndPointsHandler.createMatchMessages(new MatchMessageDTO(), ResponseStatus.BAD_REQUEST).as(FieldErrorsResponse.class);
        assertThat(errorsResponse.containsError("matchId", "errors.match_id_must_not_be_null"), is(true));
        assertThat(errorsResponse.containsError("messageText", "errors.message_text_must_not_be_null"), is(true));

        List<MatchMessageDTO> messages = MatchMessagesEndPointsHandler.getUserMessages(user.getUserId());
        assertThat(messages, hasSize(0));
    }

    @Test
    public void shouldNotAllowToUpdateMessageIfNoMatchIdOrText() {
        AuthEndPointsHandler.loginAsAdmin();
        int matchId = AdminTestDataGenerator.createRandomMatch().getMatchId();

        MatchMessageDTO matchMessage = createMatchMessage(matchId);

        matchMessage.setMatchId(0);
        matchMessage.setMessageText("");
        FieldErrorsResponse errorsResponse = MatchMessagesEndPointsHandler.updateMatchMessages(matchMessage, ResponseStatus.BAD_REQUEST).as(FieldErrorsResponse.class);
        assertThat(errorsResponse.containsError("matchId", "errors.match_id_must_not_be_null"), is(true));
        assertThat(errorsResponse.containsError("messageText", "errors.message_text_must_not_be_null"), is(true));

        List<MatchMessageDTO> messages = MatchMessagesEndPointsHandler.getMatchMessages(matchId);
        assertThat(messages, hasSize(1));
    }

    @Test
    public void shouldDeleteMatchMessageById() {
        AuthEndPointsHandler.loginAsAdmin();
        int matchId = AdminTestDataGenerator.createRandomMatch().getMatchId();
        MatchMessageDTO matchMessage = createMatchMessage(matchId);
        Boolean deleted = MatchMessagesEndPointsHandler.deleteMatchMessages(matchMessage.getId());
        assertThat(deleted, is(Boolean.TRUE));
    }

    @Test
    public void shouldIgnoreDeleteMatchMessageByWrongId() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        Boolean deleted = MatchMessagesEndPointsHandler.deleteMatchMessages(23456);
        assertThat(deleted, is(Boolean.TRUE));
    }

    private MatchMessageDTO createMatchMessage(final int matchId) {
        MatchMessageDTO message = new MatchMessageDTO();
        message.setMatchId(matchId);
        message.setMessageText("Message text");
        return MatchMessagesEndPointsHandler.createMatchMessages(message);
    }
}
