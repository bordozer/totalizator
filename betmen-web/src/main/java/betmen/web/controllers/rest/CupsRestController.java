package betmen.web.controllers.rest;

import betmen.core.entity.Cup;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.dto.dto.CupDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/cups")
public class CupsRestController {

    @Autowired
    private CupService cupService;
    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    /* Use publicCups() endpoint*/
    @Deprecated
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<CupDTO> nonAdminUserAll(final Principal principal) {
        return publicCups(principal);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cupId}/")
    public CupDTO cup(@PathVariable("cupId") final int cupId, final Principal principal) {
        final Cup cup = cupService.loadAndAssertExists(cupId);
        if (!cup.isPublicCup()) {
            throw new UnprocessableEntityException(String.format("Cup '#%d' not found", cupId));
        }
        return dtoService.transformCup(cup, userService.findByLogin(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public/")
    public List<CupDTO> publicCups(final Principal principal) {
        return dtoService.transformCups(cupService.loadPublic(), userService.findByLogin(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public/current/")
    public List<CupDTO> publicCurrentCups(final Principal principal) {
        return dtoService.transformCups(cupService.loadPublicCurrent(), userService.findByLogin(principal.getName()));
    }
}
