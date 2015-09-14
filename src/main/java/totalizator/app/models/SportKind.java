package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static totalizator.app.models.SportKind.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "sportKinds" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from SportKind c order by sportKindName"
		)
} )
public class SportKind extends AbstractEntity {

	public static final String LOAD_ALL = "sportKinds.loadAll";

	private String sportKindName;

	public String getSportKindName() {
		return sportKindName;
	}

	public void setSportKindName( final String sportKindName ) {
		this.sportKindName = sportKindName;
	}

	@Override
	public int hashCode() {
		return 31 * getId();
	}

	@Override
	public boolean equals( final Object o ) {

		if ( o == null ) {
			return false;
		}

		if ( this == o ) {
			return true;
		}

		if ( getClass() != o.getClass() ) {
			return false;
		}

		final SportKind sportKind = ( SportKind ) o;

		return sportKind.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "%s", sportKindName );
	}
}
