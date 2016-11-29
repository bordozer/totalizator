package betmen.web.controllers.rest.admin;

import betmen.core.entity.SportKind;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CategoryService;
import betmen.core.service.SportKindService;
import betmen.dto.dto.admin.SportKindEditDTO;
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
@RequestMapping("/admin/rest/sport-kinds")
public class AdminSportKindRestController {

    @Autowired
    private SportKindService sportKindService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, value = "/{sportKindId}")
    public SportKindEditDTO getItem(@PathVariable("sportKindId") final int sportKindId) {
        return convertToEditDto(sportKindService.loadAndAssertExists(sportKindId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<SportKindEditDTO> getAllItems() {
        return sportKindService.loadAll().stream().map(this::convertToEditDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public SportKindEditDTO createItem(@Validated @RequestBody final SportKindEditDTO dto) {
        assertNameDoesNotExist(dto);
        final SportKind sportKind = new SportKind();
        populateEntity(sportKind, dto);
        return convertToEditDto(sportKindService.save(sportKind));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{sportKindId}")
    public SportKindEditDTO updateItem(@PathVariable("sportKindId") final int sportKindId, @Validated @RequestBody final SportKindEditDTO dto) {
        assertNameDoesNotExist(dto);
        final SportKind sportKind = sportKindService.loadAndAssertExists(dto.getSportKindId());
        populateEntity(sportKind, dto);
        return convertToEditDto(sportKindService.save(sportKind));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{sportKindId}")
    public boolean delete(@PathVariable("sportKindId") final int sportKindId) {
        if (sportKindId == 0) {
            return true;
        }
        if (categoryService.categoriesCount(sportKindId) > 0) {
            throw new UnprocessableEntityException("Sport is assigned to at least one category.");
        }
        sportKindService.delete(sportKindId);
        return true;
    }

    private void assertNameDoesNotExist(final SportKindEditDTO dto) {
        SportKind sportKind = sportKindService.findBySportKindName(dto.getSportKindName());
        if (sportKind == null) {
            return;
        }
        if (sportKind.getId() != dto.getSportKindId()) {
            throw new UnprocessableEntityException("Sport name already exists");
        }
    }

    private SportKindEditDTO convertToEditDto(final SportKind sportKind) {
        return new SportKindEditDTO(sportKind.getId(), sportKind.getSportKindName());
    }

    private void populateEntity(final SportKind sportKind, final SportKindEditDTO dto) {
        sportKind.setSportKindName(dto.getSportKindName());
    }
}
