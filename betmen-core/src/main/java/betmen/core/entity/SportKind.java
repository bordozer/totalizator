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

import static betmen.core.entity.SportKind.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sportKinds")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from SportKind c order by sportKindName"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class SportKind extends AbstractEntity {

    public static final String LOAD_ALL = "sportKinds.loadAll";

    @Column(unique = true, nullable = false)
    private String sportKindName;

    public void setSportKindName(final String sportKindName) {
        this.sportKindName = sportKindName;
    }
}
