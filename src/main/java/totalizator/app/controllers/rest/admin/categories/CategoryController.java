package totalizator.app.controllers.rest.admin.categories;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CategoryDTO;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/category" )
public class CategoryController {

	private static final Logger LOGGER = Logger.getLogger( CategoryController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CategoryDTO> categories() {

		final List<CategoryDTO> result = newArrayList();

//		result.add( new CategoryDTO( 1, "Category 1" ) );
//		result.add( new CategoryDTO( 2, "Category 2" ) );

		return result;
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

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryDTO delete( final @RequestBody CategoryDTO categoryDTO ) {
		return categoryDTO;
	}
}
