package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import static totalizator.app.models.PointsCalculationStrategy.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "pointsCalculationStrategies" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from PointsCalculationStrategy c order by strategyName"
		)
} )
public class PointsCalculationStrategy extends AbstractEntity {

	public static final java.lang.String LOAD_ALL = "pointsCalculationStrategy.loadAll";

	@Column( columnDefinition = "VARCHAR(255)", nullable = false )
	private String strategyName;

	private int pointsForMatchScore;
	private int pointsForMatchWinner;

	private int pointsDelta;
	private int pointsForBetWithinDelta;

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName( final String strategyName ) {
		this.strategyName = strategyName;
	}

	public int getPointsForMatchScore() {
		return pointsForMatchScore;
	}

	public void setPointsForMatchScore( final int pointsForMatchScore ) {
		this.pointsForMatchScore = pointsForMatchScore;
	}

	public int getPointsForMatchWinner() {
		return pointsForMatchWinner;
	}

	public void setPointsForMatchWinner( final int pointsForMatchWinner ) {
		this.pointsForMatchWinner = pointsForMatchWinner;
	}

	public int getPointsDelta() {
		return pointsDelta;
	}

	public void setPointsDelta( final int pointsDelta ) {
		this.pointsDelta = pointsDelta;
	}

	public int getPointsForBetWithinDelta() {
		return pointsForBetWithinDelta;
	}

	public void setPointsForBetWithinDelta( final int pointsForBetWithinDelta ) {
		this.pointsForBetWithinDelta = pointsForBetWithinDelta;
	}

	@Override
	public String toString() {
		return String.format( "%s", strategyName );
	}
}
