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

import static betmen.core.entity.Team.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teams")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from Team c order by categoryId, teamName"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@ToString(of = {"teamName", "category"}, callSuper = true)
public class Team extends AbstractEntity {

    public static final String LOAD_ALL = "teams.loadAll";

    @Column(nullable = false, length = 255)
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(length = 100)
    private String logoFileName;

    @Column(length = 100)
    private String importId;

    public Team(final String teamName, final Category category) {
        this.teamName = teamName;
        this.category = category;
    }
}
