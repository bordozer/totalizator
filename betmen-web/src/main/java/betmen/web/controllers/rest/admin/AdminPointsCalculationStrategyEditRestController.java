package betmen.web.controllers.rest.admin;

import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CupService;
import betmen.core.service.PointsCalculationStrategyService;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/points-calculation-strategies")
public class AdminPointsCalculationStrategyEditRestController {

    @Autowired
    private PointsCalculationStrategyService pointsCalculationStrategyService;
    @Autowired
    private CupService cupService;

    @RequestMapping(method = RequestMethod.GET, value = "/{pcsId}")
    public PointsCalculationStrategyEditDTO getItem(@PathVariable(value = "pcsId") final int pcsId) {
        return convertToEditDto(pointsCalculationStrategyService.loadAndAssertExists(pcsId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<PointsCalculationStrategyEditDTO> getAllItems() {
        return pointsCalculationStrategyService.loadAll().stream().map(this::convertToEditDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public PointsCalculationStrategyEditDTO createItem(@Validated @RequestBody final PointsCalculationStrategyEditDTO dto) {
        assertNameDoesNotExist(dto);

        final PointsCalculationStrategy strategy = new PointsCalculationStrategy();
        populateEntityFromDto(strategy, dto);

        return convertToEditDto(pointsCalculationStrategyService.save(strategy));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{pcsId}")
    public PointsCalculationStrategyEditDTO updateItem(@PathVariable(value = "pcsId") final int pcsId, @Validated @RequestBody final PointsCalculationStrategyEditDTO dto) {
        assertNameDoesNotExist(dto);

        final PointsCalculationStrategy strategy = pointsCalculationStrategyService.loadAndAssertExists(dto.getPcsId());
        populateEntityFromDto(strategy, dto);

        return convertToEditDto(pointsCalculationStrategyService.save(strategy));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{pcsId}")
    public boolean delete(@PathVariable(value = "pcsId") final int pcsId) {
        if (pcsId == 0) {
            return true;
        }

        if (cupService.cupsCountWithPointsStrategy(pcsId) > 0) {
            throw new UnprocessableEntityException("The strategy is assigned to at least one cup");
        }

        pointsCalculationStrategyService.delete(pcsId);
        return true;
    }

    private void populateEntityFromDto(final PointsCalculationStrategy strategy, final PointsCalculationStrategyEditDTO dto) {
        strategy.setStrategyName(dto.getStrategyName());
        strategy.setPointsForMatchScore(dto.getPointsForMatchScore());
        strategy.setPointsForMatchWinner(dto.getPointsForMatchWinner());
        strategy.setPointsDelta(dto.getPointsDelta());
        strategy.setPointsForBetWithinDelta(dto.getPointsForBetWithinDelta());
    }

    private PointsCalculationStrategyEditDTO convertToEditDto(final PointsCalculationStrategy strategy) {
        final PointsCalculationStrategyEditDTO dto = new PointsCalculationStrategyEditDTO();
        dto.setPcsId(strategy.getId());
        dto.setStrategyName(strategy.getStrategyName());
        dto.setPointsForMatchScore(strategy.getPointsForMatchScore());
        dto.setPointsForMatchWinner(strategy.getPointsForMatchWinner());
        dto.setPointsDelta(strategy.getPointsDelta());
        dto.setPointsForBetWithinDelta(strategy.getPointsForBetWithinDelta());

        return dto;
    }

    private void assertNameDoesNotExist(final PointsCalculationStrategyEditDTO dto) {
        if (pointsCalculationStrategyService.findByName(dto.getStrategyName()) != null) {
            throw new UnprocessableEntityException("Cup name already exists");
        }
    }
}
