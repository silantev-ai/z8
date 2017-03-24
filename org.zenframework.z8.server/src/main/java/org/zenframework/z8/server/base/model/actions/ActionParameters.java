package org.zenframework.z8.server.base.model.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.zenframework.z8.server.base.query.Query;
import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.base.table.value.ILink;
import org.zenframework.z8.server.base.table.value.Link;
import org.zenframework.z8.server.json.Json;
import org.zenframework.z8.server.types.bool;
import org.zenframework.z8.server.types.guid;
import org.zenframework.z8.server.types.string;

public class ActionParameters {
	public String requestId;

	public Query requestQuery;

	public Query query;
	public Collection<Field> fields;
	public Collection<Field> sortFields;
	public Collection<Field> groupFields;
	public Collection<Field> groupBy;
	public Collection<Link> aggregateBy;
	public Field totalsBy;

	private Map<string, string> requestParameters = new HashMap<string, string>();

	public Field keyField;
	public ILink link;

	public ActionParameters() {
	}

	public ActionParameters(Map<string, string> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public ActionParameters(Query query) {
		this.query = query;
	}

	public ActionParameters(Query query, Collection<Field> fields) {
		this(query);
		this.fields = fields;
	}

	public Map<string, string> requestParameters() {
		return requestParameters;
	}

	public String requestParameter(string key) {
		string value = requestParameters.get(key);
		return value != null ? value.get() : null;
	}

	public guid getId() {
		String id = requestParameter(Json.id);
		return id != null ? new guid(id) : null;
	}

	public guid getRecordId() {
		String recordId = requestParameter(Json.recordId);
		return recordId != null ? new guid(recordId) : null;
	}

	public guid getGuid(string key) {
		return new guid(requestParameter(key));
	}

	public boolean getBoolean(string key) {
		return new bool(requestParameter(key)).get();
	}
}