package betmen.web.controllers.rest.admin.imports.strategies;

import betmen.core.service.matches.imports.GameImportStrategyType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/remote-games-import/strategy-types")
public class GameImportStrategyTypesController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<GameImportStrategyTypeDTO> collectRemoteGamesIds() throws IOException {
        return Arrays.stream(GameImportStrategyType.values())
                .map(strategyType -> new GameImportStrategyTypeDTO(strategyType.getId(), strategyType.getName()))
                .collect(Collectors.toList());
    }
}
