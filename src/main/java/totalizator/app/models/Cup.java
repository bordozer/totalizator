package totalizator.app.models;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static totalizator.app.models.Cup.FIND_BY_NAME;
import static totalizator.app.models.Cup.LOAD_ALL;


@Entity
@Table( name = "cups" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Cup c order by categoryId, cupName"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Cup c where cupName= :cupName"
		)
} )
public class Cup extends AbstractEntity {

	public static final String LOAD_ALL = "cups.loadAll";
	public static final String FIND_BY_NAME = "cups.findByName";

	private String cupName;
	private int categoryId;

	public Cup() {
	}

	public Cup( final String cupName, final int categoryId ) {
		this.cupName = cupName;
		this.categoryId = categoryId;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", getId(), cupName );
	}
}
