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

	private boolean publicCup;

	private LocalDateTime cupStartTime;

	@Column( unique = true, columnDefinition = "VARCHAR(100)" )
	private String logoFileName;

	// TODO: make this configurable, won't be working for football
	private int guessedRightPoints = 6;
	private int guessedPointsWithinDeltaPoints = 3;
	private int guessedRightWinners = 1;

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

	public int getGuessedRightPoints() {
		return guessedRightPoints;
	}

	public void setGuessedRightPoints( final int guessedRightPoints ) {
		this.guessedRightPoints = guessedRightPoints;
	}

	public int getGuessedPointsWithinDeltaPoints() {
		return guessedPointsWithinDeltaPoints;
	}

	public void setGuessedPointsWithinDeltaPoints( final int guessedPointsWithinDeltaPoints ) {
		this.guessedPointsWithinDeltaPoints = guessedPointsWithinDeltaPoints;
	}

	public int getGuessedRightWinners() {
		return guessedRightWinners;
	}

	public void setGuessedRightWinners( final int guessedRightWinners ) {
		this.guessedRightWinners = guessedRightWinners;
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
		return String.format( "%s: #%d: '%s'", category, getId(), cupName );
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName( final String logoFileName ) {
		this.logoFileName = logoFileName;
	}
}
