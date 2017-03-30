package betmen.core.converters;

import betmen.core.entity.Match;
import betmen.core.entity.MatchMessageEntity;
import betmen.core.entity.User;
import betmen.core.model.MatchMessage;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchMessageConverterTest {

    private static final int MESSAGE_ID = 65787;
    private static final int MATCH_ID = 457;
    private static final int USER_ID = 23;
    private static final LocalDateTime MESSAGE_TIME = LocalDateTime.of(2017, 2, 1, 16, 33);
    private static final String MESSAGE_TEXT = "Message text";

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfEntityIsNullWhenConvertToModel() {
        MatchMessageConverter.toModel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfEntityIsNullWhenPopulatesEntity() {
        MatchMessageConverter.populateEntity(null, new MatchMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfModelIsNullWhenPopulatesEntity() {
        MatchMessageConverter.populateEntity(new MatchMessageEntity(), null);
    }

    @Test
    public void shouldConvertToEntity() {
        // given
        MatchMessageEntity entity = constructMatchMessageEntity();

        // when
        MatchMessage model = MatchMessageConverter.toModel(entity);

        // then
        assertThat(model, notNullValue());
        assertThat(model.getId(), is(MESSAGE_ID));
        assertThat(model.getMatchId(), is(MATCH_ID));
        assertThat(model.getUserId(), is(USER_ID));
        assertThat(model.getMessageTime(), is(MESSAGE_TIME));
        assertThat(model.getMessageText(), is(MESSAGE_TEXT));
    }

    @Test
    public void shouldPopulateEntity() {
        // given
        MatchMessageEntity entity = constructMatchMessageEntity();

        MatchMessage model = constructMatchMessageModel();

        // when
        MatchMessageConverter.populateEntity(entity, model);

        // then
        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(MESSAGE_ID));
        assertThat(entity.getMessageTime(), is(MESSAGE_TIME));
        assertThat(entity.getMessageText(), is(MESSAGE_TEXT));
        assertThat(entity.getMatch(), is(entity.getMatch()));
        assertThat(entity.getUser(), is(entity.getUser()));
    }

    @Test
    public void shouldPopulateEntityWithMandatoryFields() {
        // given
        MatchMessageEntity entity = constructMatchMessageEntity();
        entity.setId(455);

        MatchMessage model = new MatchMessage();
        model.setMessageText("Test of message");

        // when
        MatchMessageConverter.populateEntity(entity, model);

        // then
        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(455));
        assertThat(entity.getMessageTime(), is(MESSAGE_TIME));
        assertThat(entity.getMessageText(), is("Test of message"));
        assertThat(entity.getMatch(), is(entity.getMatch()));
        assertThat(entity.getUser(), is(entity.getUser()));
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

    private MatchMessageEntity constructMatchMessageEntity() {
        Match match = new Match();
        match.setId(MATCH_ID);

        User user = new User();
        user.setId(USER_ID);

        MatchMessageEntity entity = new MatchMessageEntity();
        entity.setId(MESSAGE_ID);
        entity.setMatch(match);
        entity.setUser(user);
        entity.setMessageTime(MESSAGE_TIME);
        entity.setMessageText(MESSAGE_TEXT);
        return entity;
    }
}