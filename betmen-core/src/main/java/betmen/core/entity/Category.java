package betmen.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.Category.FIND_BY_NAME;
import static betmen.core.entity.Category.FIND_BY_SPORT_KIND;
import static betmen.core.entity.Category.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "categories")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from Category c order by categoryName"
        ),
        @NamedQuery(
                name = FIND_BY_NAME,
                query = "select c from Category c where categoryName= :categoryName"
        ),
        @NamedQuery(
                name = FIND_BY_SPORT_KIND,
                query = "select c from Category c where sportKindId= :sportKindId"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Category extends AbstractEntity {

    public static final String LOAD_ALL = "categories.loadAll";
    public static final String FIND_BY_NAME = "categories.findByName";
    public static final String FIND_BY_SPORT_KIND = "categories.findBySportKind";

    @Column(unique = true, length = 255, nullable = false)
    private String categoryName;

    @Column(unique = true, length = 100)
    private String logoFileName;

    @ManyToOne
    @JoinColumn(name = "sportKindId", nullable = false)
    private SportKind sportKind;

    private int remoteGameImportStrategyTypeId;

    public Category(final String categoryName) {
        this.categoryName = categoryName;
    }
}
