package totalizator.app.controllers.rest.admin.categories;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CategoryDTO;
import totalizator.app.models.Category;
import totalizator.app.services.CategoryService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/category" )
public class CategoryController {

	private static final Logger LOGGER = Logger.getLogger( CategoryController.class );

	@Autowired
	private CategoryService categoryService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CategoryDTO> categories() {

		final List<CategoryDTO> result = newArrayList();

//		result.add( new CategoryDTO( 1, "Category 1" ) );
//		result.add( new CategoryDTO( 2, "Category 2" ) );

		return result;
	}

	/*@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}", produces = APPLICATION_JSON_VALUE )
	public CategoryDTO category( final @PathVariable( "categoryId" ) int categoryId, final @RequestBody CategoryDTO categoryDTO ) {
		return categoryDTO;
	}*/

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO create( final @RequestBody CategoryDTO categoryDTO ) {
		// TODO: check if name exists
		categoryService.save( new Category( categoryDTO.getCategoryName() ) );

		return categoryDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{categoryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO edit( final @PathVariable( "categoryId" ) int categoryId, final @RequestBody CategoryDTO categoryDTO ) {
		// TODO: check if name exists
		final Category category = categoryService.load( categoryDTO.getCategoryId() );
		category.setCategoryName( categoryDTO.getCategoryName() );

		categoryService.save( category );

		return categoryDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{categoryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO delete( final @PathVariable( "categoryId" ) int categoryId, final @RequestBody CategoryDTO categoryDTO ) {
		return categoryDTO;
	}
}
