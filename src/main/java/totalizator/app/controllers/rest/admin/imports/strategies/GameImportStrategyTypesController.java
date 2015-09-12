package totalizator.app.controllers.rest.admin.imports.strategies;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.services.matches.imports.GameImportStrategyType;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping( "/admin/rest/remote-games-import/strategy-types" )
public class GameImportStrategyTypesController {

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<GameImportStrategyTypeDTO> collectRemoteGamesIds() throws IOException {

		final List<GameImportStrategyTypeDTO> result = newArrayList();

		for ( final GameImportStrategyType strategyType : GameImportStrategyType.values() ) {
			result.add( new GameImportStrategyTypeDTO( strategyType.getId(), strategyType.getName() ) );
		}

		return result;
	}
}
