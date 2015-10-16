package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import totalizator.app.models.converters.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.UserGroup.LOAD_ALL;
import static totalizator.app.models.UserGroup.LOAD_ALL_USER_IS_OWNER;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "userGroups" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select g from UserGroup g order by groupName"
		),
		@NamedQuery(
				name = LOAD_ALL_USER_IS_OWNER,
				query = "select g from UserGroup g where userId= :userId order by groupName"
		)
} )
public class UserGroup extends AbstractEntity {

	public static final String LOAD_ALL = "userGroup.loadAll";
	public static final String LOAD_ALL_USER_IS_OWNER = "userGroup.loadAllWhereUserIsOwner";

	@Column( columnDefinition = "VARCHAR(100)" )
	private String groupName;

	@ManyToOne
	@JoinColumn( name = "userId" )
	private User owner;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName( final String groupName ) {
		this.groupName = groupName;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner( final User owner ) {
		this.owner = owner;
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

		if ( !( obj instanceof UserGroup ) ) {
			return false;
		}

		final UserGroup group = ( UserGroup ) obj;
		return group.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "%d: %s ( %s )", getId(), groupName, owner );
	}
}
