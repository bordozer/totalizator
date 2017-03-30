package betmen.core.service;


import betmen.core.entity.Match;
import betmen.core.entity.MatchMessageEntity;
import betmen.core.entity.User;
import betmen.core.model.MatchMessage;
import betmen.core.repository.jpa.MatchMessageJpaRepository;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeServiceImpl;
import com.beust.jcommander.internal.Lists;
import org.easymock.EasyMock;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class MatchMessageServiceImplTest {

    private static final int MESSAGE_ID = 65787;
    private static final int MATCH_ID = 457;
    private static final int USER_ID = 23;
    private static final LocalDateTime MESSAGE_TIME = LocalDateTime.of(2017, 2, 1, 16, 33);
    private static final String MESSAGE_TEXT = "Message text";

    @Test
    public void shouldFindAllMessagesForMatch() {
        // given
        MatchMessageServiceImpl sut = initMatchMessageServiceImpl();

        // when
        List<MatchMessage> messages = sut.findAllByMatchId(MATCH_ID);

        // then
        assertThat(messages, notNullValue());
        assertThat(messages.size(), is(1));

        MatchMessage matchMessage = messages.get(0);
        assertThat(matchMessage.getId(), is(MESSAGE_ID));
        assertModel(matchMessage);
    }

    @Test
    public void shouldFindAllMessagesOfUser() {
        // given
        MatchMessageServiceImpl sut = initMatchMessageServiceImpl();

        // when
        List<MatchMessage> messages = sut.findAllByUserId(USER_ID);

        // then
        assertThat(messages, notNullValue());
        assertThat(messages.size(), is(1));

        MatchMessage matchMessage = messages.get(0);
        assertThat(matchMessage.getId(), is(MESSAGE_ID));
        assertModel(matchMessage);
    }

    @Test
    public void shouldCreateMessage() {
        // given
        MatchMessage model = constructMatchMessageModel();
        User user = new User();
        user.setId(USER_ID);

        MatchMessageServiceImpl sut = initMatchMessageServiceImpl();

        // when
        MatchMessage matchMessage = sut.create(model, user);

        // then
        assertThat(matchMessage, notNullValue());

        assertThat(matchMessage.getId(), is(MESSAGE_ID));
        assertModel(matchMessage);
    }

    private void assertModel(final MatchMessage matchMessage) {
        assertThat(matchMessage.getMatchId(), is(MATCH_ID));
        assertThat(matchMessage.getUserId(), is(USER_ID));
        assertThat(matchMessage.getMessageTime(), is(MESSAGE_TIME));
        assertThat(matchMessage.getMessageText(), is(MESSAGE_TEXT));
    }

    private MatchMessageServiceImpl initMatchMessageServiceImpl() {
        MatchMessageServiceImpl sut = new MatchMessageServiceImpl();
        sut.setDateTimeService(getDateTimeServiceMock());
        sut.setMatchMessageJpaRepository(getMatchMessageJpaRepositoryMock());
        sut.setMatchService(getMatchServiceMock());
        return sut;
    }

    private DateTimeServiceImpl getDateTimeServiceMock() {
        return new DateTimeServiceImpl();
    }

    private MatchMessageJpaRepository getMatchMessageJpaRepositoryMock() {
        MatchMessageEntity entity = constructMatchMessageEntity();
        List<MatchMessageEntity> entities = Lists.newArrayList(entity);

        final MatchMessageJpaRepository mock = EasyMock.createMock(MatchMessageJpaRepository.class);

        EasyMock.expect(mock.findAllByMatchId(MATCH_ID)).andReturn(entities).anyTimes();
        EasyMock.expect(mock.findAllByUserId(USER_ID)).andReturn(entities).anyTimes();
        EasyMock.expect(mock.save(entity)).andReturn(entity).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(mock);

        return mock;
    }

    private MatchService getMatchServiceMock() {
        Match match = new Match();
        match.setId(MATCH_ID);

        final MatchService mock = EasyMock.createMock(MatchService.class);

        EasyMock.expect(mock.load(MATCH_ID)).andReturn(match).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(mock);

        return mock;
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