package betmen.web.controllers.rest.admin.jobs.results;

import betmen.core.service.matches.imports.RemoteGame;
import betmen.dto.dto.MatchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteGameMatchPair {
    private final RemoteGame remoteGame;
    private boolean isNewGameCreated;
    private int matchId;
    private MatchDTO match;

    public RemoteGameMatchPair(final RemoteGame remoteGame) {
        this.remoteGame = remoteGame;
    }
}
