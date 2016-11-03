package betmen.core.service.matches;

import betmen.core.entity.Match;
import betmen.core.model.MatchSearchModel;
import betmen.core.repository.jpa.MatchJpaRepository;
import betmen.core.repository.specifications.MnBWidgetMatchesSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.util.List;

@Service
public class MatchesAndBetsWidgetServiceImpl implements MatchesAndBetsWidgetService {

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Override
    public List<Match> loadAll(final MatchSearchModel searchQuery) {
        Assert.assertTrue(searchQuery.getCupId() > 0, "Cup ID should be provided");
        Assert.assertTrue(searchQuery.isShowFinished() || searchQuery.isShowFutureMatches(), "Finished or future matches or both type should be selected");
        return matchJpaRepository.findAll(new MnBWidgetMatchesSpecification(searchQuery), sort(searchQuery.getSorting()));
    }

    private Sort sort(final int sorting) {
        return new Sort(new Order(direction(sorting), "beginningTime"));
    }

    private Direction direction(final int sorting) {
        return sorting == 1 ? Direction.ASC : Direction.DESC;
    }
}
