package totalizator.app.controllers.ui.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.CategoryService;

@Controller
@RequestMapping( "/totalizator/categories" )
public class CategoryController {

	public static final String MODEL_NAME = "categoryModel";

	private static final String VIEW = "/Category";

	@Autowired
	private CategoryService categoryService;

	@ModelAttribute( MODEL_NAME )
	public CategoryModel preparePagingModel() {
		return new CategoryModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}/" )
	public String portalPage( final @PathVariable( "categoryId" ) int categoryId, final @ModelAttribute( MODEL_NAME ) CategoryModel model ) {

		model.setCategory( categoryService.load( categoryId ) );

		return VIEW;
	}
}
