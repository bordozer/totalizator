package totalizator.app.models;

import javax.persistence.*;

import static totalizator.app.models.Team.FIND_BY_NAME;
import static totalizator.app.models.Team.LOAD_ALL;


@Entity
@Table( name = "teams" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Team c order by categoryId, teamName"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Team c where teamName= :teamName"
		)
} )
public class Team extends AbstractEntity {

	public static final String LOAD_ALL = "teams.loadAll";
	public static final String FIND_BY_NAME = "teams.findByName";

	@Column( unique = true, columnDefinition = "VARCHAR(255)" )
	private String teamName;

	private int categoryId;

	public Team() {
	}

	public Team( final String teamName, final int categoryId ) {
		this.teamName = teamName;
		this.categoryId = categoryId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName( final String teamName ) {
		this.teamName = teamName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", getId(), teamName );
	}
}
