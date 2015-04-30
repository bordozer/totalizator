package totalizator.app.controllers.rest.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CategoryDTO;
import totalizator.app.services.CategoryService;
import totalizator.app.services.DTOService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/categories" )
public class CategoriesRestController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CategoryDTO> entries() {
		return dtoService.transformCategories( categoryService.loadAll() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}/", produces = APPLICATION_JSON_VALUE )
	public CategoryDTO entry( final @PathVariable( "categoryId" ) int categoryId ) {
		return dtoService.transformCategory( categoryService.load( categoryId ) );
	}
}
