package totalizator.app.models;

import javax.persistence.*;

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

	@Column( unique = true, columnDefinition = "VARCHAR(255)" )
	private String cupName;

	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;

	private boolean showOnPortalPage;

	public Cup() {
	}

	public Cup( final String cupName, final Category category ) {
		this.cupName = cupName;
		this.category = category;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory( final Category category ) {
		this.category = category;
	}

	public boolean isShowOnPortalPage() {
		return showOnPortalPage;
	}

	public void setShowOnPortalPage( final boolean showOnPortalPage ) {
		this.showOnPortalPage = showOnPortalPage;
	}

	@Override
	public int hashCode() {
		return 31 * getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof Cup ) ) {
			return false;
		}

		final Cup cup = ( Cup ) obj;
		return cup.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", getId(), cupName );
	}
}
