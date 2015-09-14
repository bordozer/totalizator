package totalizator.app.controllers.rest.sportKindCups;

import totalizator.app.dto.CategoryDTO;
import totalizator.app.dto.CupDTO;

import java.util.List;

public class CategoryCupsDTO {

	private CategoryDTO category;
	private List<CupDTO> cups;

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory( final CategoryDTO category ) {
		this.category = category;
	}

	public List<CupDTO> getCups() {
		return cups;
	}

	public void setCups( final List<CupDTO> cups ) {
		this.cups = cups;
	}
}
