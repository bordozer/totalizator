package betmen.web.controllers.rest;

import betmen.core.service.SportKindService;
import betmen.dto.dto.SportKindDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/sport-kinds")
public class SportKindsRestController {

    @Autowired
    private SportKindService sportKindService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<SportKindDTO> all() {
        return dtoService.transformSportKinds(sportKindService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{sportKindId}/")
    public SportKindDTO entry(final @PathVariable("sportKindId") int sportKindId) {
        return dtoService.transformSportKind(sportKindService.loadAndAssertExists(sportKindId));
    }
}
