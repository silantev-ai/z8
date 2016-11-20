package org.zenframework.z8.server.db.sql.functions.date;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.IValue;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;

public class TruncQuarter extends SqlToken {
    private SqlToken param1;

    public TruncQuarter(SqlToken p1) {
        param1 = p1;
    }

    @Override
    public void collectFields(Collection<IValue> fields) {
        param1.collectFields(fields);
    }

    @Override
    public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
        switch(vendor) {
        case Oracle:
            return "Trunc(" + param1.format(vendor, options) + ", 'Q')";
        case Postgres:
            return "date_trunc('quarter', " + param1.format(vendor, options) + ")";
        case SqlServer:
            return "Convert(datetime, cast(year(" + param1.format(vendor, options)
                    + ") as char) +'/'+ cast(((DATEPART ( q , " + param1.format(vendor, options)
                    + ")-1)*3+1) as char)+ '/01', 120)";
        default:
            throw new UnknownDatabaseException();
        }
    }

    @Override
    public FieldType type() {
        return param1.type();
    }
}