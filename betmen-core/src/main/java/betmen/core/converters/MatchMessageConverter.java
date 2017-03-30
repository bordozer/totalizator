package betmen.core.converters;

import betmen.core.entity.MatchMessageEntity;
import betmen.core.model.MatchMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchMessageConverter {

    public static MatchMessage toModel(final MatchMessageEntity entity) {
        Assert.notNull(entity, "MatchMessageEntity should not be null");

        MatchMessage model = new MatchMessage();
        model.setId(entity.getId());
        model.setMatchId(entity.getMatch().getId());
        model.setUserId(entity.getUser().getId());
        model.setMessageTime(entity.getMessageTime());
        model.setMessageText(entity.getMessageText());
        return model;
    }

    public static void populateEntity(final MatchMessageEntity entity, final MatchMessage matchMessage) {
        Assert.notNull(entity, "MatchMessage entity should not be null");
        Assert.notNull(matchMessage, "MatchMessage model should not be null");
        entity.setMessageText(matchMessage.getMessageText());
    }
}
