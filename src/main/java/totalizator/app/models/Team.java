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

	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;

	public Team() {
	}

	public Team( final String teamName, final Category category ) {
		this.teamName = teamName;
		this.category = category;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName( final String teamName ) {
		this.teamName = teamName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory( final Category category ) {
		this.category = category;
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", getId(), teamName );
	}
}
