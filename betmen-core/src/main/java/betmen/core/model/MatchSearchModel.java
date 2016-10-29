package betmen.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchSearchModel {

    private int userId;
    private int cupId;
    private int teamId;
    private int team2Id;

    private String filterByDate;
    private boolean filterByDateEnable;

    private boolean showFutureMatches;
    private boolean showFinished;

    private int sorting; // 1 is ASC, all remains are DESC

    // the field does not matter for backend
    private int categoryId;

    @Override
    public String toString() {
        return String.format("User: %d, Category: %d; cup: %d; team: %d", userId, categoryId, cupId, teamId);
    }
}
