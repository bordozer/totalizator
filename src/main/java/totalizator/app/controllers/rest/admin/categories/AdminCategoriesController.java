package totalizator.app.controllers.rest.admin.categories;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import totalizator.app.models.Category;
import totalizator.app.services.CategoryService;
import totalizator.app.services.LogoService;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/categories" )
public class AdminCategoriesController {

	private static final Logger LOGGER = Logger.getLogger( AdminCategoriesController.class );

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private LogoService logoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CategoryEditDTO> entries() {

		return Lists.transform( categoryService.loadAll(), new Function<Category, CategoryEditDTO>() {
			@Override
			public CategoryEditDTO apply( final Category category ) {

				final CategoryEditDTO categoryEditDTO = new CategoryEditDTO( category.getId(), category.getCategoryName() );
				categoryEditDTO.setLogoUrl( logoService.getLogoURL( category ) );

				return categoryEditDTO;
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryEditDTO create( final @RequestBody CategoryEditDTO categoryEditDTO ) {
		// TODO: check if name exists
		final Category category = categoryService.save( new Category( categoryEditDTO.getCategoryName() ) );

		categoryEditDTO.setCategoryId( category.getId() );

		return categoryEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{categoryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CategoryEditDTO edit( final @PathVariable( "categoryId" ) int categoryId, final @RequestBody CategoryEditDTO categoryEditDTO ) {
		// TODO: check if name exists
		final Category category = categoryService.load( categoryEditDTO.getCategoryId() );
		category.setCategoryName( categoryEditDTO.getCategoryName() );

		categoryService.save( category );

		return categoryEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{categoryId}" )
	public void delete( final @PathVariable( "categoryId" ) int categoryId ) {

		if ( categoryId == 0 ) {
			return;
		}

		categoryService.delete( categoryId );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{categoryId}/logo/" )
	public void uploadLogo( final @PathVariable( "categoryId" ) int categoryId, final MultipartHttpServletRequest request ) throws IOException {

		final Category cup = categoryService.load( categoryId );

		final Iterator<String> itr = request.getFileNames();
		if ( ! itr.hasNext() ) {
			return;
		}

		final MultipartFile logoFile = request.getFile( itr.next() );

		cup.setLogoFileName( String.format( "category_logo_%d", cup.getId() ) );
		categoryService.save( cup );

		logoFile.transferTo( logoService.getLogoFile( cup ) );
	}
}
