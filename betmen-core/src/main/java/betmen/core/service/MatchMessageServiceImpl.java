package betmen.core.service;

import betmen.core.converters.MatchMessageConverter;
import betmen.core.entity.MatchMessageEntity;
import betmen.core.entity.User;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.model.MatchMessage;
import betmen.core.repository.jpa.MatchMessageJpaRepository;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchMessageServiceImpl implements MatchMessageService {

    @Autowired
    private MatchMessageJpaRepository matchMessageJpaRepository;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private MatchService matchService;

    @Override
    public List<MatchMessage> findAllByMatchId(@NotNull final int matchId) {
        return convertToModels(matchMessageJpaRepository.findAllByMatchId(matchId));
    }

    @Override
    public List<MatchMessage> findAllByUserId(@NotNull final int userId) {
        return convertToModels(matchMessageJpaRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    public MatchMessage create(@NotNull final MatchMessage matchMessage, @NotNull final User user) {
        MatchMessageEntity entity = new MatchMessageEntity();
        entity.setMatch(matchService.loadAndAssertExists(matchMessage.getMatchId()));
        entity.setUser(user);
        entity.setMessageTime(dateTimeService.getNow());

        MatchMessageConverter.populateEntity(entity, matchMessage);

        return MatchMessageConverter.toModel(matchMessageJpaRepository.save(entity));
    }

    @Override
    @Transactional
    public MatchMessage update(@NotNull final MatchMessage matchMessage, @NotNull final User user) {
        MatchMessageEntity entity = matchMessageJpaRepository.findAllByIdAndUserId(matchMessage.getId(), user.getId());
        if (entity == null) {
            throw new UnprocessableEntityException("errors.message_not_found");
        }
        MatchMessageConverter.populateEntity(entity, matchMessage);
        return MatchMessageConverter.toModel(matchMessageJpaRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(final int messageId, final User user) {
        matchMessageJpaRepository.deleteOneByIdAndUserId(messageId, user.getId());
    }

    private List<MatchMessage> convertToModels(final List<MatchMessageEntity> messages) {
        return messages.stream()
                .map(MatchMessageConverter::toModel)
                .collect(Collectors.toList());
    }

    public void setMatchMessageJpaRepository(final MatchMessageJpaRepository matchMessageJpaRepository) {
        this.matchMessageJpaRepository = matchMessageJpaRepository;
    }

    public void setDateTimeService(final DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public void setMatchService(final MatchService matchService) {
        this.matchService = matchService;
    }
}
