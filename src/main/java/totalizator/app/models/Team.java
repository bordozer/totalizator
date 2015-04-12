package totalizator.app.models;

import javax.persistence.*;

import static totalizator.app.models.Team.*;


@Entity
@Table( name = "teams" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Team c order by categoryId, teamName"
		),
		@NamedQuery(
				name = FIND_BY_CATEGORY,
				query = "select c from Team c where categoryId= :categoryId order by teamName"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Team c where teamName= :teamName"
		)
} )
public class Team extends AbstractEntity {

	public static final String LOAD_ALL = "teams.loadAll";
	public static final String FIND_BY_CATEGORY = "matches.findByCategory";
	public static final String FIND_BY_NAME = "teams.findByName";

	@Column( unique = false, columnDefinition = "VARCHAR(255)" )
	private String teamName;

	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;

	@Column( unique = true, columnDefinition = "VARCHAR(30)" )
	private String logoFileName;

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

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName( final String logoFileName ) {
		this.logoFileName = logoFileName;
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", getId(), teamName );
	}
}
