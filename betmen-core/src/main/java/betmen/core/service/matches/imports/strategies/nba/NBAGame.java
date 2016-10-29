package betmen.core.service.matches.imports.strategies.nba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NBAGame {

    private List<LinkedTreeMap<Object, Object>> resultSets;

    public List<LinkedTreeMap<Object, Object>> getResultSets() {
        return resultSets;
    }

    public void setResultSets(final List<LinkedTreeMap<Object, Object>> resultSets) {
        this.resultSets = resultSets;
    }
}
