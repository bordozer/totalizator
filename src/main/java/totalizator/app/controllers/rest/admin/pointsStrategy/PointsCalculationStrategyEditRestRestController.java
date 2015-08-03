package totalizator.app.controllers.rest.admin.pointsStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.models.User;
import totalizator.app.services.PointsCalculationStrategyService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/points-calculation-strategies" )
public class PointsCalculationStrategyEditRestRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private PointsCalculationStrategyService pointsCalculationStrategyService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<PointsCalculationStrategyEditDTO> entries( final Principal principal ) {
		return transformPCStrategies( pointsCalculationStrategyService.loadAll(), getUser( principal ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE )
	public PointsCalculationStrategyEditDTO create( final @RequestBody PointsCalculationStrategyEditDTO dto, final Principal principal ) {
		final PointsCalculationStrategy strategy = new PointsCalculationStrategy();
		fromDTO( dto, strategy );

		final PointsCalculationStrategy saved = pointsCalculationStrategyService.save( strategy );

		return getMapper( getUser( principal ) ).apply( saved );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{entryId}", produces = APPLICATION_JSON_VALUE )
	public PointsCalculationStrategyEditDTO save( final @PathVariable( value = "entryId" ) int entryId, final @RequestBody PointsCalculationStrategyEditDTO dto, final Principal principal ) {

		final PointsCalculationStrategy strategy = pointsCalculationStrategyService.load( dto.getPcsId() );
		// TODO: if strategy == null

		fromDTO( dto, strategy );

		final PointsCalculationStrategy saved = pointsCalculationStrategyService.save( strategy );

		return getMapper( getUser( principal ) ).apply( pointsCalculationStrategyService.load( saved.getId() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{entryId}" )
	public void delete( final @PathVariable( value = "entryId" ) int entryId ) {

		if ( entryId == 0 ) {
			return;
		}

		// TODO: validate no using of

		pointsCalculationStrategyService.delete( entryId );
	}

	private void fromDTO( final PointsCalculationStrategyEditDTO dto, final PointsCalculationStrategy strategy ) {
		strategy.setStrategyName( dto.getStrategyName() );
		strategy.setPointsForMatchScore( dto.getPointsForMatchScore() );
		strategy.setPointsForMatchWinner( dto.getPointsForMatchWinner() );
		strategy.setPointsDelta( dto.getPointsDelta() );
		strategy.setPointsForBetWithinDelta( dto.getPointsForBetWithinDelta() );
	}

	private Function<PointsCalculationStrategy, PointsCalculationStrategyEditDTO> getMapper( final User currentUser ) {

		return new Function<PointsCalculationStrategy, PointsCalculationStrategyEditDTO>() {

			@Override
			public PointsCalculationStrategyEditDTO apply( final PointsCalculationStrategy strategy ) {

				final PointsCalculationStrategyEditDTO dto = new PointsCalculationStrategyEditDTO();
				dto.setPcsId( strategy.getId() );
				dto.setStrategyName( strategy.getStrategyName() );
				dto.setPointsForMatchScore( strategy.getPointsForMatchScore() );
				dto.setPointsForMatchWinner( strategy.getPointsForMatchWinner() );
				dto.setPointsDelta( strategy.getPointsDelta() );
				dto.setPointsForBetWithinDelta( strategy.getPointsForBetWithinDelta() );

				return dto;
			}
		};
	}

	private List<PointsCalculationStrategyEditDTO> transformPCStrategies( final List<PointsCalculationStrategy> strategies, final User currentUser ) {
		return strategies.stream().map( getMapper( currentUser ) ).collect( Collectors.toList() );
	}

	private User getUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
