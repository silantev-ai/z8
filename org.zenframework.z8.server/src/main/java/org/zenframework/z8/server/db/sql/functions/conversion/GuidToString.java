package org.zenframework.z8.server.db.sql.functions.conversion;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.base.table.value.IValue;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlField;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;

public class GuidToString extends SqlToken {
	private SqlToken guid;

	public GuidToString(Field field) {
		this(new SqlField(field));
	}

	public GuidToString(SqlToken guid) {
		this.guid = guid;
	}

	@Override
	public void collectFields(Collection<IValue> fields) {
		guid.collectFields(fields);
	}

	@Override
	public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
		String param = guid.format(vendor, options);

		switch(vendor) {
		case Postgres:
			return param + "::text";
		case Oracle:
			return "SUBSTR(RAWTOHEX(" + param + "), 0, 8)||'-'||SUBSTR(RAWTOHEX(" + param + "), 9, 4)||'-'||SUBSTR(RAWTOHEX(" + param + "), 13, 4)||'-'||SUBSTR(RAWTOHEX(" + param + "), 17, 4)||'-'||SUBSTR(RAWTOHEX(" + param + "), 21, 12)";
		case SqlServer:
			return "Convert(nvarchar(40), " + param + ")";
		default:
			throw new UnknownDatabaseException();
		}
	}

	@Override
	public FieldType type() {
		return FieldType.String;
	}
}