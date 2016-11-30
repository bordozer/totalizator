package betmen.core.service.matches;

import betmen.core.entity.Match;
import betmen.core.exception.BadRequestException;
import betmen.core.model.MatchSearchModel;
import betmen.core.repository.jpa.MatchJpaRepository;
import betmen.core.repository.specifications.MnBWidgetMatchesSpecification;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchesAndBetsWidgetServiceImpl implements MatchesAndBetsWidgetService {

    @Autowired
    private MatchJpaRepository matchJpaRepository;
    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public List<Match> loadAll(final MatchSearchModel searchQuery) {
        if (searchQuery.getCupId() <= 0) {
            throw new BadRequestException("Cup should be provided");
        }
        if (!(searchQuery.isShowFinished() || searchQuery.isShowFutureMatches())) {
            throw new BadRequestException("Finished or future matches or both type should be selected");
        }
        return matchJpaRepository.findAll(new MnBWidgetMatchesSpecification(searchQuery, dateTimeService), sort(searchQuery.getSorting()));
    }

    private Sort sort(final int sorting) {
        return new Sort(new Order(direction(sorting), "beginningTime"));
    }

    private Direction direction(final int sorting) {
        return sorting == 1 ? Direction.ASC : Direction.DESC;
    }
}
