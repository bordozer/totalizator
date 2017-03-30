package betmen.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "MATCH_MESSAGE")
@Getter
@Setter
@EqualsAndHashCode(of = {}, callSuper = true)
@ToString
@NoArgsConstructor
public class MatchMessageEntity extends AbstractEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "MATCH_ID")
    private Match match;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "MESSAGE_TIME", nullable = false, updatable = false)
    private LocalDateTime messageTime;

    @Column(name = "MESSAGE", nullable = false)
    private String messageText;
}
