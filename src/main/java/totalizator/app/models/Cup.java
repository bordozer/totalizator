package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.Cup.FIND_BY_NAME;
import static totalizator.app.models.Cup.LOAD_ALL;
import static totalizator.app.models.Cup.LOAD_ALL_USE_STRATEGY;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "cups" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Cup c order by categoryId, cupStartTime desc"
		),
		@NamedQuery(
				name = FIND_BY_NAME,
				query = "select c from Cup c where cupName= :cupName"
		),
		@NamedQuery(
				name = LOAD_ALL_USE_STRATEGY,
				query = "select c from Cup c where pointsCalculationStrategyId= :strategyId"
		)
} )
public class Cup extends AbstractEntity {

	public static final String LOAD_ALL = "cups.loadAll";
	public static final String FIND_BY_NAME = "cups.findByName";
	public static final java.lang.String LOAD_ALL_USE_STRATEGY = "cups.loadAllForStrategy";

	@Column( columnDefinition = "VARCHAR(255)" )
	private String cupName;

	@ManyToOne
	@JoinColumn( name = "categoryId" )
	private Category category;

	private int winnersCount;

	private boolean publicCup;

	private LocalDateTime cupStartTime;

	@Column( unique = true, columnDefinition = "VARCHAR(100)" )
	private String logoFileName;

	@ManyToOne
	@JoinColumn( name = "pointsCalculationStrategyId" )
	private PointsCalculationStrategy pointsCalculationStrategy;

	@Column( columnDefinition = "VARCHAR(100)" )
	private String cupImportId;

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

	public boolean isPublicCup() {
		return publicCup;
	}

	public void setPublicCup( final boolean publicCup ) {
		this.publicCup = publicCup;
	}

	public LocalDateTime getCupStartTime() {
		return cupStartTime;
	}

	public void setCupStartTime( final LocalDateTime cupStartTime ) {
		this.cupStartTime = cupStartTime;
	}

	public String getCupImportId() {
		return cupImportId;
	}

	public void setCupImportId( final String cupImportId ) {
		this.cupImportId = cupImportId;
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

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName( final String logoFileName ) {
		this.logoFileName = logoFileName;
	}

	public PointsCalculationStrategy getPointsCalculationStrategy() {
		return pointsCalculationStrategy;
	}

	public void setPointsCalculationStrategy( final PointsCalculationStrategy pointsCalculationStrategy ) {
		this.pointsCalculationStrategy = pointsCalculationStrategy;
	}

	@Override
	public String toString() {
		return String.format( "%s: #%d: '%s'", category, getId(), cupName );
	}
}
