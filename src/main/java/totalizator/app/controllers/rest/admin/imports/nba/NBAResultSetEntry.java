package totalizator.app.controllers.rest.admin.imports.nba;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

public class NBAResultSetEntry {

	private LinkedTreeMap<Object, Object> name;
	private LinkedTreeMap<Object, List<Object>> headers;
	private List<Object> rowSet;

	public LinkedTreeMap<Object, Object> getName() {
		return name;
	}

	public void setName( final LinkedTreeMap<Object, Object> name ) {
		this.name = name;
	}

	public LinkedTreeMap<Object, List<Object>> getHeaders() {
		return headers;
	}

	public void setHeaders( final LinkedTreeMap<Object, List<Object>> headers ) {
		this.headers = headers;
	}

	public List<Object> getRowSet() {
		return rowSet;
	}

	public void setRowSet( final List<Object> rowSet ) {
		this.rowSet = rowSet;
	}
}
