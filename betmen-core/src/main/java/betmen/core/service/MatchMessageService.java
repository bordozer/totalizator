package betmen.core.service;

import betmen.core.entity.User;
import betmen.core.model.MatchMessage;

import java.util.List;

public interface MatchMessageService {

    List<MatchMessage> findAllByMatchId(int matchId);

    List<MatchMessage> findAllByUserId(int userId);

    MatchMessage create(MatchMessage matchMessage, final User user);

    MatchMessage update(MatchMessage matchMessage, User user);

    void delete(int messageId, User user);
}
