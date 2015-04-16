package totalizator.app.controllers.rest.admin.imports.nba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class NBAGame {

	private List<LinkedTreeMap<Object, NBAResultSetEntry>> resultSets;

	public List<LinkedTreeMap<Object, NBAResultSetEntry>> getResultSets() {
		return resultSets;
	}

	public void setResultSets( final List<LinkedTreeMap<Object, NBAResultSetEntry>> resultSets ) {
		this.resultSets = resultSets;
	}
}
