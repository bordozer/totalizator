package betmen.core.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static betmen.core.entity.MatchPoints.LOAD_ALL;
import static betmen.core.entity.MatchPoints.LOAD_ALL_FOR_MATCH;
import static betmen.core.entity.MatchPoints.LOAD_ALL_FOR_MATCH_AND_USER;
import static betmen.core.entity.MatchPoints.LOAD_ALL_FOR_PERIOD;
import static betmen.core.entity.MatchPoints.LOAD_ALL_FOR_USER_AND_MATCH;
import static betmen.core.entity.MatchPoints.LOAD_ALL_FOR_USER_AND_MATCH_AND_GROUP;
import static betmen.core.entity.MatchPoints.LOAD_SUMMARY_FOR_USER_AND_CUP;
import static betmen.core.entity.MatchPoints.LOAD_SUMMARY_FOR_USER_AND_CUP_AND_GROUP;
import static betmen.core.entity.MatchPoints.LOAD_SUMMARY_FOR_USER_FOR_CUP_FOR_PERIOD;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(
        name = "matchPoints"
        , indexes = {
        @Index(name = "matchId_userId_idx", columnList = "matchId,userId")
        , @Index(name = "userId_matchId_groupId_idx", columnList = "userId,matchId,userGroupId", unique = true)
        , @Index(name = "matchId_groupId_idx", columnList = "matchId,userGroupId")
}
)
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from MatchPoints c"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_MATCH,
                query = "select c from MatchPoints c where matchId = :matchId and userGroupId is null"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_MATCH_AND_USER,
                query = "select c from MatchPoints c where matchId = :matchId and userGroupId = :userGroupId"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_USER_AND_MATCH,
                query = "select c from MatchPoints c where userId = :userId and matchId = :matchId and userGroupId is null"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_USER_AND_MATCH_AND_GROUP,
                query = "select c from MatchPoints c where userId = :userId and matchId = :matchId and userGroupId = :userGroupId"
        ),
        @NamedQuery(
                name = LOAD_ALL_FOR_PERIOD,
                query = "select mp from MatchPoints mp where mp.matchTime >= :timeFrom and mp.matchTime <= :timeTo and userGroupId is null"
        ),
        @NamedQuery(
                name = LOAD_SUMMARY_FOR_USER_FOR_CUP_FOR_PERIOD,
                query = "select sum( matchPoints ) as matchPoints, sum( matchBonus ) as matchBonus  from MatchPoints mp where userId = :userId and cupId = :cupId and mp.matchTime >= :timeFrom and mp.matchTime <= :timeTo and userGroupId is null"
        ),
        @NamedQuery(
                name = LOAD_SUMMARY_FOR_USER_AND_CUP,
                query = "select sum( matchPoints ) as matchPoints, sum( matchBonus ) as matchBonus from MatchPoints where userId = :userId and cupId = :cupId and userGroupId is null"
        ),
        @NamedQuery(
                name = LOAD_SUMMARY_FOR_USER_AND_CUP_AND_GROUP,
                query = "select sum( matchPoints ) as matchPoints, sum( matchBonus ) as matchBonus from MatchPoints where userId = :userId and cupId = :cupId and userGroupId = :userGroupId"
        )
})
public class MatchPoints extends AbstractEntity {

    public static final String LOAD_ALL = "matchPoints.loadAll";

    public static final String LOAD_ALL_FOR_MATCH = "matchPoints.LOAD_ALL_FOR_MATCH";
    public static final String LOAD_ALL_FOR_MATCH_AND_USER = "matchPoints.LOAD_ALL_FOR_MATCH_AND_USER";

    public static final String LOAD_ALL_FOR_USER_AND_MATCH = "matchPoints.LOAD_ALL_FOR_USER_AND_MATCH";
    public static final String LOAD_ALL_FOR_USER_AND_MATCH_AND_GROUP = "matchPoints.LOAD_ALL_FOR_USER_AND_MATCH_AND_GROUP";

    public static final String LOAD_ALL_FOR_PERIOD = "matchPoints.LOAD_ALL_FOR_PERIOD";
    public static final String LOAD_SUMMARY_FOR_USER_FOR_CUP_FOR_PERIOD = "matchPoints.LOAD_ALL_FOR_USER_FOR_CUP_FOR_PERIOD";

    public static final String LOAD_SUMMARY_FOR_USER_AND_CUP = "matchPoints.LOAD_ALL_FOR_USER_AND_CUP";
    public static final String LOAD_SUMMARY_FOR_USER_AND_CUP_AND_GROUP = "matchPoints.LOAD_ALL_FOR_USER_AND_CUP_AND_GROUP";

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cupId")
    private Cup cup;

    @ManyToOne
    @JoinColumn(name = "matchId")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "userGroupId")
    private UserGroup userGroup;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime matchTime;

    private int matchPoints;
    private float matchBonus;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Cup getCup() {
        return cup;
    }

    public void setCup(final Cup cup) {
        this.cup = cup;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(final Match match) {
        this.match = match;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(final LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }

    public int getMatchPoints() {
        return matchPoints;
    }

    public void setMatchPoints(final int matchPoints) {
        this.matchPoints = matchPoints;
    }

    public float getMatchBonus() {
        return matchBonus;
    }

    public void setMatchBonus(final float matchBonus) {
        this.matchBonus = matchBonus;
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        final MatchPoints matchPoints = (MatchPoints) obj;
        return matchPoints.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %s ( %s + %s )", user, match, matchPoints, matchBonus);
    }
}
