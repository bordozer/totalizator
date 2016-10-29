package betmen.core.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.CupTeam.LOAD_ALL;
import static betmen.core.entity.CupTeam.LOAD_ALL_CUP_TEAMS;
import static betmen.core.entity.CupTeam.LOAD_CUP_TEAM;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(
        name = "cupTeams",
        indexes = {
                @Index(name = "idx_cupId_teamId", columnList = "cupId,teamId", unique = true)
        })
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from CupTeam c"
        ),
        @NamedQuery(
                name = LOAD_ALL_CUP_TEAMS,
                query = "select c from CupTeam c where cupId= :cupId"
        ),
        @NamedQuery(
                name = LOAD_CUP_TEAM,
                query = "select c from CupTeam c where cupId= :cupId and teamId= :teamId"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class CupTeam extends AbstractEntity {

    public static final String LOAD_ALL = "cupTeams.loadAll";
    public static final String LOAD_ALL_CUP_TEAMS = "cupTeams.loadAllForCup";
    public static final String LOAD_CUP_TEAM = "cupTeams.loadCupTeam";

    @ManyToOne
    @JoinColumn(name = "cupId")
    private Cup cup;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;
}
