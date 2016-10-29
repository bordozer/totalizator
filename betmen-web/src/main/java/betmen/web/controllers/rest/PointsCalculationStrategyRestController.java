package betmen.web.controllers.rest;

import betmen.core.service.CupService;
import betmen.core.service.PointsCalculationStrategyService;
import betmen.core.service.UserService;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.PointsCalculationStrategyDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/points-calculation-strategies")
public class PointsCalculationStrategyRestController {

    @Autowired
    private PointsCalculationStrategyService pointsCalculationStrategyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CupService cupService;

    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<PointsCalculationStrategyDTO> entries() {
        return dtoService.transformPCStrategies(pointsCalculationStrategyService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{pcsId}/cups/")
    public List<CupDTO> cups(@PathVariable(value = "pcsId") final int pcsId, final Principal principal) {
        return dtoService.transformCups(cupService.loadCups(pointsCalculationStrategyService.loadAndAssertExists(pcsId)), userService.findByLogin(principal.getName()));
    }
}
