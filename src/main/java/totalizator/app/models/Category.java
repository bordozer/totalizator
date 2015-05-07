package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import static totalizator.app.models.Category.FIND_BY_NAME;
import static totalizator.app.models.Category.LOAD_ALL;

@Entity
//@Cacheable( true )
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "categories" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Category c order by categoryName"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Category c where categoryName= :categoryName"
		)
} )
public class Category extends AbstractEntity {

	public static final String LOAD_ALL = "categories.loadAll";
	public static final String FIND_BY_NAME = "categories.findByName";

	@Column( unique = true, columnDefinition = "VARCHAR(255)" )
	private String categoryName;

	@Column( unique = true, columnDefinition = "VARCHAR(100)" )
	private String logoFileName;

	public Category() {
	}

	public Category( final String categoryName ) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName( final String categoryName ) {
		this.categoryName = categoryName;
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName( final String logoFileName ) {
		this.logoFileName = logoFileName;
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

		if ( !( obj instanceof Category ) ) {
			return false;
		}

		final Category category = ( Category ) obj;
		return category.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", getId(), categoryName );
	}
}
