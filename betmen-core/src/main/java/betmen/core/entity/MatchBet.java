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
import java.time.LocalDateTime;

import static betmen.core.entity.MatchBet.LOAD_ALL;
import static betmen.core.entity.MatchBet.LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER;
import static betmen.core.entity.MatchBet.LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET;
import static betmen.core.entity.MatchBet.LOAD_COUNT_OF_MATCHES_WITH_USER_BET;
import static betmen.core.entity.MatchBet.LOAD_FOR_MATCH;
import static betmen.core.entity.MatchBet.LOAD_FOR_USER;
import static betmen.core.entity.MatchBet.LOAD_FOR_USER_AND_MATCH;
import static betmen.core.entity.MatchBet.LOAD_MATCH_BETS_COUNT;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(
        name = "matchBets",
        indexes = {
                @Index(name = "matchId_userId_idx", columnList = "matchId,userId", unique = true)
        }
)
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select mb from MatchBet mb order by betTime desc"
        ),
        @NamedQuery(
                name = LOAD_FOR_USER,
                query = "select mb from MatchBet mb where userId= :userId order by betTime desc"
        ),
        @NamedQuery(
                name = LOAD_FOR_MATCH,
                query = "select mb from MatchBet mb where matchId= :matchId order by betTime desc"
        ),
        @NamedQuery(
                name = LOAD_FOR_USER_AND_MATCH,
                query = "select mb from MatchBet mb where userId= :userId and matchId= :matchId order by betTime desc"
        ),
        @NamedQuery(
                name = LOAD_MATCH_BETS_COUNT,
                query = "select count( id ) from MatchBet mb where matchId= :matchId"
        ),
        @NamedQuery(
                name = LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET,
                query = "select count( mb.id ) from MatchBet as mb join mb.match as m where mb.user.id = :userId and m.cup.id = :cupId"
        ),
        @NamedQuery(
                name = LOAD_COUNT_OF_MATCHES_WITH_USER_BET,
                query = "select count( mb.id ) from MatchBet as mb join mb.match as m where mb.user.id = :userId"
        ),
        @NamedQuery(
                name = LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER,
                query = "select count( m.id ) from Match as m where m.cup.id = :cupId and m.beginningTime >= :time and m.id not in ( select mb.match.id from MatchBet mb where mb.user.id = :userId )"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MatchBet extends AbstractEntity {

    public static final String LOAD_ALL = "matchBets.loadAll";
    public static final String LOAD_FOR_USER = "matchBets.loadForUser";
    public static final String LOAD_FOR_USER_ON_DATE = "matchBets.LOAD_FOR_USER_ON_DATE";
    public static final String LOAD_FOR_MATCH = "matchBets.loadForMatch";
    public static final String LOAD_FOR_USER_AND_MATCH = "matchBets.loadForUserAndMatch";
    public static final String LOAD_MATCH_BETS_COUNT = "matchBets.matchBetsCount";

    public static final String LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET = "matchBets.LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET";
    public static final String LOAD_COUNT_OF_MATCHES_WITH_USER_BET = "matchBets.LOAD_COUNT_OF_MATCHES_WITH_USER_BET";
    public static final String LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER = "matchBets.LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER";

    @ManyToOne
    @JoinColumn(name = "matchId", updatable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false)
    private User user;

    private int betScore1;
    private int betScore2;

    private LocalDateTime betTime;
}
