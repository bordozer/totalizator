package betmen.web.converters;

import betmen.core.model.MatchMessage;
import betmen.dto.dto.MatchMessageDTO;
import betmen.dto.dto.UserDTO;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchMessageDtoConverterTest {

    private static final int MESSAGE_ID = 65787;
    private static final int MATCH_ID = 457;
    private static final int USER_ID = 23;
    private static final String USER_NAME = "Harden";
    private static final LocalDateTime MESSAGE_TIME = LocalDateTime.of(2017, 2, 1, 16, 33);
    private static final String MESSAGE_TEXT = "Message text";

    @Test
    public void shouldConvertToDto() {
        // given
        MatchMessage model = constructMatchMessageModel();

        // when
        final UserDTO user = new UserDTO();
        user.setUserId(USER_ID);
        user.setUserName(USER_NAME);

        MatchMessageDTO dto = MatchMessageDtoConverter.convertToDto(model, user);

        // then
        assertThat(dto, notNullValue());
        assertThat(dto.getId(), is(MESSAGE_ID));
        assertThat(dto.getMatchId(), is(MATCH_ID));
        assertThat(dto.getMessageTime(), is(MESSAGE_TIME));
        assertThat(dto.getMessageText(), is(MESSAGE_TEXT));

        assertThat(dto.getUser(), notNullValue());
        assertThat(dto.getUser().getUserId(), is(USER_ID));
        assertThat(dto.getUser().getUserName(), is(USER_NAME));
    }

    @Test
    public void shouldConvertToModel() {
        // given
        MatchMessageDTO dto = new MatchMessageDTO();
        dto.setId(MESSAGE_ID);
        dto.setMatchId(MATCH_ID);
        dto.setMessageTime(MESSAGE_TIME);
        dto.setMessageText(MESSAGE_TEXT);

        // when
        MatchMessage model = MatchMessageDtoConverter.convertToModel(dto);

        // then
        assertThat(model, notNullValue());
        assertThat(model.getId(), is(MESSAGE_ID));
        assertThat(model.getMatchId(), is(MATCH_ID));
        assertThat(model.getMessageTime(), nullValue());
        assertThat(model.getMessageText(), is(MESSAGE_TEXT));
    }

    private MatchMessage constructMatchMessageModel() {
        MatchMessage model = new MatchMessage();
        model.setId(MESSAGE_ID);
        model.setMatchId(MATCH_ID);
        model.setUserId(USER_ID);
        model.setMessageTime(MESSAGE_TIME);
        model.setMessageText(MESSAGE_TEXT);
        return model;
    }
}