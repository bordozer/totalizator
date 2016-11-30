package betmen.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static betmen.core.entity.Cup.FIND_BY_NAME;
import static betmen.core.entity.Cup.LOAD_ALL;
import static betmen.core.entity.Cup.LOAD_ALL_USE_STRATEGY;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cups")
@NamedQueries({
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
                query = "select c from Cup c where pointsCalculationStrategyId= :strategyId order by cupStartTime desc"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@ToString(of = {"cupName", "category"}, callSuper = true)
public class Cup extends AbstractEntity {

    public static final String LOAD_ALL = "cups.loadAll";
    public static final String FIND_BY_NAME = "cups.findByName";
    public static final java.lang.String LOAD_ALL_USE_STRATEGY = "cups.loadAllForStrategy";

    @Column(nullable = false, length = 255)
    private String cupName;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    private int winnersCount;

    private boolean publicCup;

    @Column(nullable = false)
    private LocalDateTime cupStartTime;

    @Column(unique = true, length = 100)
    private String logoFileName;

    @ManyToOne
    @JoinColumn(name = "pointsCalculationStrategyId", nullable = false)
    private PointsCalculationStrategy pointsCalculationStrategy;

    @Column(length = 100)
    private String cupImportId;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "cup",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<CupWinner> cupWinners = new ArrayList<>();

    public Cup(final String cupName, final Category category) {
        this.cupName = cupName;
        this.category = category;
    }
}
