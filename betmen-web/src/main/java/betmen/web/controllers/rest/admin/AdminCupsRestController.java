package betmen.web.controllers.rest.admin;

import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.imports.GameImportStrategyType;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.admin.CupForGameImportDTO;
import betmen.web.converters.DTOService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/admin/rest/cups")
public class AdminCupsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CupService cupService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{cupId}/")
    public CupDTO getCup(final @PathVariable("cupId") int cupId, final Principal principal) {
        return dtoService.transformCup(cupService.load(cupId), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<CupDTO> allCups(final Principal principal) {
        return dtoService.transformCups(allCups(), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/current/")
    public List<CupDTO> currentCupsOnly(final Principal principal) {
        return dtoService.transformCups(currentCupsOnly(), getUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/")
    public List<CupForGameImportDTO> importableCups(final @RequestParam("sportKindId") int sportKindId, final Principal principal) {
        return getCupForGameImportDTOs(allCups(), sportKindId, principal);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/current/")
    public List<CupForGameImportDTO> importableCurrentCups(final @RequestParam("sportKindId") int sportKindId, final Principal principal) {
        return getCupForGameImportDTOs(currentCupsOnly(), sportKindId, principal);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/for-category/{categoryId}/")
    public List<CupDTO> getCategoryCups(final @PathVariable("categoryId") int categoryId, final Principal principal) {
        return dtoService.transformCups(cupService.load(categoryService.load(categoryId)), getUser(principal));
    }

    private List<CupForGameImportDTO> getCupForGameImportDTOs(final List<Cup> cups, final int sportKindId, final Principal principal) {
        return cups
                .stream()
                .filter(getImportStrategiesPredicate())
                .filter(getSportKindPredicate(sportKindId))
                .sorted(cupService.categoryNameOrCupNameComparator())
                .map(cup -> getCupForGameImportDTO(cup, principal))
                .collect(Collectors.toList());
    }

    private CupForGameImportDTO getCupForGameImportDTO(final Cup cup, final Principal principal) {
        CupDTO cupDto = dtoService.transformCup(cup, getUser(principal));
        GameImportStrategyType strategyType = GameImportStrategyType.getById(cup.getCategory().getRemoteGameImportStrategyTypeId());
        return new CupForGameImportDTO(cupDto, strategyType.getTimePeriodType());
    }

    private List<Cup> allCups() {
        final List<Cup> result = newArrayList();
        result.addAll(cupService.loadPublic());
        result.addAll(cupService.loadHidden());
        return result;
    }

    private List<Cup> currentCupsOnly() {
        final List<Cup> result = newArrayList();
        result.addAll(cupService.loadPublicCurrent());
        result.addAll(cupService.loadHiddenCurrent());
        return result;
    }

    // TODO: mode to service
    // TODO: cup on save validation
    // TODO: TEST!!!
    private Predicate<Cup> getImportStrategiesPredicate() {
        return cup -> {
            final GameImportStrategyType strategyType = GameImportStrategyType.getById(cup.getCategory().getRemoteGameImportStrategyTypeId());
            if (GameImportStrategyType.CUP_ID_NEEDED.contains(strategyType) && StringUtils.isEmpty(cup.getCupImportId())) {
                return false;
            }
            return strategyType != GameImportStrategyType.NO_IMPORT;
        };
    }

    private Predicate<Cup> getSportKindPredicate(final int sportKindId) {
        return cup -> cup.getCategory().getSportKind().getId() == sportKindId;
    }

    private User getUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
