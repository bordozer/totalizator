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
import java.time.LocalDateTime;

import static betmen.core.entity.Match.FIND_ALL_TEAM_MATCHES_FOR_CUP;
import static betmen.core.entity.Match.FIND_BY_CUP;
import static betmen.core.entity.Match.FIND_BY_CUP_AND_TEAMS;
import static betmen.core.entity.Match.FIND_BY_CUP_AND_TEAMS_FINISHED;
import static betmen.core.entity.Match.FIND_BY_TEAM;
import static betmen.core.entity.Match.FIND_BY_TEAMS;
import static betmen.core.entity.Match.FIND_CUP_MATCHES_BY_DATE;
import static betmen.core.entity.Match.FIND_FINISHED_MATCHES;
import static betmen.core.entity.Match.FIND_MATCHES_BY_DATE;
import static betmen.core.entity.Match.FIND_MATCH_BY_REMOTE_GAME_ID;
import static betmen.core.entity.Match.FIND_NOT_FINISHED_MATCHES;
import static betmen.core.entity.Match.FIND_NOT_FINISHED_MATCHES_STARTED_AFTER;
import static betmen.core.entity.Match.FIND_NOT_FINISHED_MATCHES_STARTED_TILL;
import static betmen.core.entity.Match.LAST_IMPORTED_MATCH;
import static betmen.core.entity.Match.LOAD_ALL;
import static betmen.core.entity.Match.LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM;
import static betmen.core.entity.Match.LOAD_FUTURE_MATCH_COUNT_FOR_CUP;
import static betmen.core.entity.Match.LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM;
import static betmen.core.entity.Match.LOAD_LAST_TEAM_MATCHES;
import static betmen.core.entity.Match.LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM;
import static betmen.core.entity.Match.LOAD_MATCH_COUNT_FOR_TEAM;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "matches")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from Match c order by cupId, beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_BY_CUP,
                query = "select c from Match c where cupId= :cupId order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_BY_TEAM,
                query = "select c from Match c where ( team1Id= :teamId ) or ( team2Id= :teamId ) order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_BY_TEAMS,
                query = "select c from Match c where ( team1Id= :team1Id and team2Id= :team2Id ) or (team1Id= :team2Id and team2Id= :team1Id) order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_BY_CUP_AND_TEAMS,
                query = "select c from Match c where ( cupId= :cupId ) and  ( ( team1Id= :team1Id and team2Id= :team2Id ) or ( team1Id= :team2Id and team2Id= :team1Id ) ) order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_BY_CUP_AND_TEAMS_FINISHED,
                query = "select c from Match c where ( cupId= :cupId ) and ( matchFinished = true ) and ( ( team1Id= :team1Id and team2Id= :team2Id ) or ( team1Id= :team2Id and team2Id= :team1Id ) ) order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_ALL_TEAM_MATCHES_FOR_CUP,
                query = "select c from Match c where ( cupId= :cupId ) and ( team1Id= :teamId or team2Id= :teamId ) order by beginningTime desc"
        ),
        @NamedQuery(
                name = FIND_MATCH_BY_REMOTE_GAME_ID,
                query = "select c from Match c where remoteGameId= :remoteGameId"
        ),
        @NamedQuery(
                name = LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM,
                query = "select count(m) from Match m where ( cupId= :cupId ) and ( team1Id= :teamId or team2Id= :teamId )"
        ),
        @NamedQuery(
                name = LOAD_MATCH_COUNT_FOR_TEAM,
                query = "select count(m) from Match m where ( team1Id= :teamId or team2Id= :teamId )"
        ),
        @NamedQuery(
                name = LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM,
                query = "select count(m) from Match m where ( cupId= :cupId ) and ( matchFinished = true ) and ( team1Id= :teamId or team2Id= :teamId )"
        ),
        @NamedQuery(
                name = LOAD_FUTURE_MATCH_COUNT_FOR_CUP,
                query = "select count(m) from Match m where cupId= :cupId and matchFinished = false"
        ),
        @NamedQuery(
                name = LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM,
                query = "select count(m) from Match m where ( cupId= :cupId ) and ( matchFinished = false ) and ( team1Id= :teamId or team2Id= :teamId )"
        ),
        @NamedQuery(
                name = LOAD_LAST_TEAM_MATCHES,
                query = "select m from Match m where ( cupId= :cupId ) and ( matchFinished = true ) and ( team1Id= :teamId or team2Id= :teamId ) order by m.beginningTime DESC"
        ),
        @NamedQuery(
                name = LAST_IMPORTED_MATCH,
                query = "select m from Match m where ( cupId = :cupId ) and ( remoteGameId IS NOT NULL ) order by m.beginningTime DESC, m.remoteGameId DESC"
        ),
        @NamedQuery(
                name = FIND_MATCHES_BY_DATE,
                query = "select m from Match m where beginningTime BETWEEN :timeFrom AND :timeTo"
        ),
        @NamedQuery(
                name = FIND_CUP_MATCHES_BY_DATE,
                query = "select m from Match m where cupId = :cupId and beginningTime BETWEEN :timeFrom AND :timeTo"
        ),
        @NamedQuery(
                name = FIND_NOT_FINISHED_MATCHES_STARTED_TILL,
                query = "select m from Match m where cupId = :cupId and ( matchFinished = false ) and beginningTime < :time"
        ),
        @NamedQuery(
                name = FIND_NOT_FINISHED_MATCHES_STARTED_AFTER,
                query = "select m from Match m where cupId = :cupId and ( matchFinished = false ) and beginningTime > :time"
        ),
        @NamedQuery(
                name = FIND_NOT_FINISHED_MATCHES,
                query = "select m from Match m where cupId = :cupId and matchFinished = false order by beginningTime"
        ),
        @NamedQuery(
                name = FIND_FINISHED_MATCHES,
                query = "select m from Match m where cupId = :cupId and matchFinished = true order by beginningTime desc"
        )
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@ToString(of = {"cup", "beginningTime", "team1", "team2"}, callSuper = true)
public class Match extends AbstractEntity {

    public static final String LOAD_ALL = "matches.loadAll";
    public static final String FIND_BY_CUP = "matches.findByCup";
    public static final String FIND_BY_TEAM = "matches.findByTeam";
    public static final String FIND_BY_TEAMS = "matches.findByTeams";
    public static final String FIND_BY_CUP_AND_TEAMS = "matches.findByCupAndTeams";
    public static final String FIND_BY_CUP_AND_TEAMS_FINISHED = "matches.FIND_BY_CUP_AND_TEAMS_FINISHED";
    public static final String FIND_ALL_TEAM_MATCHES_FOR_CUP = "matches.findByCupAndTeam";
    public static final String FIND_MATCH_BY_REMOTE_GAME_ID = "matches.findByRemoteGameId";
    public static final String LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.loadTeamMatchCountForCup";
    public static final String LOAD_MATCH_COUNT_FOR_TEAM = "cups.loadTeamMatchCount";
    public static final String LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.finishedMatchCountForCupAndTeam";
    public static final String LOAD_FUTURE_MATCH_COUNT_FOR_CUP = "cups.futureMatchCountForCup";
    public static final String LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.futureMatchCountForCupAndTeam";

    public static final String FIND_MATCHES_BY_DATE = "cups.loadMatchesInPeriod";
    public static final String FIND_CUP_MATCHES_BY_DATE = "cups.loadCupMatchesInPeriod";

    public static final String FIND_NOT_FINISHED_MATCHES_STARTED_TILL = "cups.futureMatchesStartedSince";
    public static final String FIND_NOT_FINISHED_MATCHES_STARTED_AFTER = "cups.futureNearestMatch";

    public static final String FIND_NOT_FINISHED_MATCHES = "cups.FIND_NOT_FINISHED_MATCHES";
    public static final String FIND_FINISHED_MATCHES = "cups.FIND_FINISHED_MATCHES";
    public static final String LOAD_LAST_TEAM_MATCHES = "cups.LOAD_LAST_TEAM_MATCHES";
    public static final String LAST_IMPORTED_MATCH = "cups.LAST_IMPORTED_MATCH";

    @ManyToOne
    @JoinColumn(name = "cupId", nullable = false)
    private Cup cup;

    @ManyToOne
    @JoinColumn(name = "team1Id", nullable = false)
    private Team team1;
    private int score1;

    @ManyToOne
    @JoinColumn(name = "team2Id", nullable = false)
    private Team team2;
    private int score2;

    @Column(nullable = false)
    private LocalDateTime beginningTime;

    private boolean matchFinished;
    private int homeTeamNumber;

    // TODO: is too small for real description, but hsqldb:mem:mydb does not have TEXT data type. Just a temporary solution
    @Column(length = 255)
    private String description;

    @Column(length = 100)
    private String remoteGameId;
}
