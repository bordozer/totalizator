package totalizator.app.models;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.Cup.FIND_BY_NAME;
import static totalizator.app.models.Cup.LOAD_ALL;


@Entity
@Table( name = "cups" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Cup c order by categoryId, cupStartTime desc"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Cup c where cupName= :cupName"
		)
} )
public class Cup extends AbstractEntity {

	public static final String LOAD_ALL = "cups.loadAll";
	public static final String FIND_BY_NAME = "cups.findByName";

	@Column( columnDefinition = "VARCHAR(255)" ) //unique = true,
	private String cupName;

	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;

	private int winnersCount;

	private boolean showOnPortalPage;

	private LocalDateTime cupStartTime;

	private boolean readyForCupBets;
	private boolean readyForMatchBets;
	private boolean finished;

	@Column( unique = true, columnDefinition = "VARCHAR(100)" )
	private String logoFileName;

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

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	public boolean isShowOnPortalPage() {
		return showOnPortalPage;
	}

	public void setShowOnPortalPage( final boolean showOnPortalPage ) {
		this.showOnPortalPage = showOnPortalPage;
	}

	public LocalDateTime getCupStartTime() {
		return cupStartTime;
	}

	public void setCupStartTime( final LocalDateTime cupStartTime ) {
		this.cupStartTime = cupStartTime;
	}

	public boolean isReadyForCupBets() {
		return readyForCupBets;
	}

	public void setReadyForCupBets( final boolean readyForCupBets ) {
		this.readyForCupBets = readyForCupBets;
	}

	public boolean isReadyForMatchBets() {
		return readyForMatchBets;
	}

	public void setReadyForMatchBets( final boolean readyForMatchBets ) {
		this.readyForMatchBets = readyForMatchBets;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
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

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName( final String logoFileName ) {
		this.logoFileName = logoFileName;
	}
}
