package totalizator.app.controllers.rest.admin.categories;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CategoryDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/category" )
public class CategoryController {

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}", produces = APPLICATION_JSON_VALUE )
	public CategoryDTO categories( final @PathVariable( "categoryId" ) int categoryId ) {

		final CategoryDTO dto = new CategoryDTO();
		dto.setCategoryId( categoryId ); // TODO: load category

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO create( final @RequestBody CategoryDTO categoryDTO ) {
		return categoryDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO edit( final @RequestBody CategoryDTO categoryDTO ) {
		return categoryDTO;
	}
}
