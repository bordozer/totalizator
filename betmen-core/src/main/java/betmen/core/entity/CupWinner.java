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

import static betmen.core.entity.CupWinner.LOAD_ALL;
import static betmen.core.entity.CupWinner.LOAD_FOR_CUP;
import static betmen.core.entity.CupWinner.LOAD_FOR_CUP_AND_TEAM;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(
        name = "cupWinners"
        /*, indexes = {
                @Index(columnList = "cupId,teamId", name = "idx_cup_team", unique = true)
                , @Index(columnList = "cupId,cupPosition", name = "idx_cup_position", unique = true)
        }*/
)
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select t from CupWinner t order by cupPosition desc"
        ),
        @NamedQuery(
                name = LOAD_FOR_CUP,
                query = "select t from CupWinner t where cupId= :cupId order by cupPosition"
        ),
        @NamedQuery(
                name = LOAD_FOR_CUP_AND_TEAM,
                query = "select t from CupWinner t where cupId= :cupId and teamId= :teamId"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"cup", "cupPosition", "team"}, callSuper = true)
public class CupWinner extends AbstractEntity {

    public static final String LOAD_ALL = "cupWinners.loadAll";
    public static final String LOAD_FOR_CUP = "cupWinners.loadForCup";
    public static final String LOAD_FOR_CUP_AND_TEAM = "cupWinners.loadForCupAndTeam";

    @ManyToOne
    @JoinColumn(name = "cupId", nullable = false)
    private Cup cup;

    @ManyToOne
    @JoinColumn(name = "teamId", nullable = false)
    private Team team;

    @Column(nullable = false)
    private int cupPosition;
}
