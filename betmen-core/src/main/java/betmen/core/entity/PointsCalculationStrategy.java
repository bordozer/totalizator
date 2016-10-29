package betmen.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.PointsCalculationStrategy.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "pointsCalculationStrategies")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from PointsCalculationStrategy c order by strategyName"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class PointsCalculationStrategy extends AbstractEntity {

    public static final java.lang.String LOAD_ALL = "pointsCalculationStrategy.loadAll";

    @Column(length = 255, nullable = false, unique = true)
    private String strategyName;
    private int pointsForMatchScore;
    private int pointsForMatchWinner;
    private int pointsDelta;
    private int pointsForBetWithinDelta;
}
