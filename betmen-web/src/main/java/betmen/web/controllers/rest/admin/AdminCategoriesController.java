package betmen.web.controllers.rest.admin;

import betmen.core.entity.Category;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.LogoService;
import betmen.core.service.SportKindService;
import betmen.core.service.TeamService;
import betmen.dto.dto.admin.CategoryEditDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/categories")
public class AdminCategoriesController {

    private static final Logger LOGGER = Logger.getLogger(AdminCategoriesController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SportKindService sportKindService;
    @Autowired
    private LogoService logoService;
    @Autowired
    private CupService cupService;

    @RequestMapping(method = RequestMethod.GET, value = "/{categoryId}")
    public CategoryEditDTO getItem(final @PathVariable("categoryId") int categoryId) {
        return convertToEditDto(categoryService.loadAndAssertExists(categoryId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<CategoryEditDTO> getAllItems() {
        return categoryService.loadAll().stream().map(this::convertToEditDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public CategoryEditDTO createItem(final @Validated @RequestBody CategoryEditDTO dto) {
        assertNameDoesNotExist(dto);
        final Category category = new Category();
        populateEntity(category, dto);
        return convertToEditDto(categoryService.save(category));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{categoryId}")
    public CategoryEditDTO updateItem(final @PathVariable("categoryId") int categoryId, final @Validated @RequestBody CategoryEditDTO dto) {
        assertNameDoesNotExist(dto);
        Category category = categoryService.loadAndAssertExists(dto.getCategoryId());
        populateEntity(category, dto);
        return convertToEditDto(categoryService.save(category));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{categoryId}")
    public boolean delete(final @PathVariable("categoryId") int categoryId) {
        if (categoryId == 0) {
            return true;
        }

        LOGGER.debug(String.format("About to delete category #%d", categoryId));

        if (!CollectionUtils.isEmpty(teamService.loadAll(categoryId))) {
            LOGGER.debug(String.format("Category %d is assigned to at least one team. Can not delete", categoryId));
            throw new UnprocessableEntityException("Category is assigned to at least one team. Can not delete");
        }

        if (cupService.cupsCount(categoryId) > 0) {
            LOGGER.debug(String.format("Category %d is assigned to at least one cup", categoryId));
            throw new UnprocessableEntityException("Category is assigned to at least one cup.");
        }

        Category category = categoryService.load(categoryId);
        categoryService.delete(categoryId);
        LOGGER.info(String.format("Category %s has been deleted", category));

        try {
            logoService.deleteLogo(category);
        } catch (IOException e) {
            LOGGER.debug(String.format("Category #%s logo does not exist", category));
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/logo/")
    public void uploadLogo(final @PathVariable("categoryId") int categoryId, final MultipartHttpServletRequest request) throws IOException {
        Category category = categoryService.loadAndAssertExists(categoryId);
        final Iterator<String> itr = request.getFileNames();
        if (!itr.hasNext()) {
            return;
        }

        final MultipartFile logoFile = request.getFile(itr.next());

        category.setLogoFileName(String.format("category_logo_%d", category.getId()));
        categoryService.save(category);

        logoFile.transferTo(logoService.getLogoFile(category));
    }

    private void assertNameDoesNotExist(final CategoryEditDTO dto) {
        Category category = categoryService.findByCategoryName(dto.getCategoryName());
        if (category == null) {
            return;
        }
        if (category.getId() != dto.getCategoryId()) {
            throw new UnprocessableEntityException("League name already exists");
        }
    }

    private CategoryEditDTO convertToEditDto(final Category category) {
        final CategoryEditDTO categoryEditDTO = new CategoryEditDTO(category.getId(), category.getCategoryName());
        categoryEditDTO.setLogoUrl(logoService.getLogoURL(category));
        categoryEditDTO.setRemoteGameImportStrategyTypeId(category.getRemoteGameImportStrategyTypeId());
        categoryEditDTO.setSportKindId(category.getSportKind().getId());
        return categoryEditDTO;
    }

    private void populateEntity(final Category category, final CategoryEditDTO dto) {
        category.setCategoryName(dto.getCategoryName());
        category.setRemoteGameImportStrategyTypeId(dto.getRemoteGameImportStrategyTypeId());
        category.setSportKind(sportKindService.loadAndAssertExists(dto.getSportKindId()));
    }
}
