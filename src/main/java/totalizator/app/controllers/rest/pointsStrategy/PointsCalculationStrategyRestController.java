package totalizator.app.controllers.rest.pointsStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.PointsCalculationStrategyDTO;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.PointsCalculationStrategyService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/points-calculation-strategies" )
public class PointsCalculationStrategyRestController {

	@Autowired
	private PointsCalculationStrategyService pointsCalculationStrategyService;

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<PointsCalculationStrategyDTO> entries() {
		return dtoService.transformPCStrategies( pointsCalculationStrategyService.loadAll() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{strategyId}/cups/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> cups( final @PathVariable( value = "strategyId" ) int strategyId, final Principal principal ) {
		return dtoService.transformCups( cupService.loadCups( pointsCalculationStrategyService.load( strategyId ) ), userService.findByLogin( principal.getName() ) );
	}
}
