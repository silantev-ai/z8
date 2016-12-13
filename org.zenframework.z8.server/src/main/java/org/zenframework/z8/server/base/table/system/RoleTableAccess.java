package org.zenframework.z8.server.base.table.system;

import org.zenframework.z8.server.base.table.Table;
import org.zenframework.z8.server.base.table.value.BoolField;
import org.zenframework.z8.server.base.table.value.Link;
import org.zenframework.z8.server.resources.Resources;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.types.bool;

public class RoleTableAccess extends Table {
	final static public String TableName = "SystemRoleTableAccess";

	static public class names {
		public final static String Role = "Role";
		public final static String Table = "Table";
		public final static String Read = "Read";
		public final static String Write = "Write";
		public final static String Create = "Create";
		public final static String Copy = "Copy";
		public final static String Destroy = "Destroy";
	}

	static public class strings {
		public final static String Title = "RoleTableAccess.title";
		public final static String Read = "RoleTableAccess.read";
		public final static String Write = "RoleTableAccess.write";
		public final static String Create = "RoleTableAccess.create";
		public final static String Copy = "RoleTableAccess.copy";
		public final static String Destroy = "RoleTableAccess.destroy";
	}

	static public class displayNames {
		public final static String Title = Resources.get(strings.Title);
		public final static String Read = Resources.get(strings.Read);
		public final static String Write = Resources.get(strings.Write);
		public final static String Create = Resources.get(strings.Create);
		public final static String Copy = Resources.get(strings.Copy);
		public final static String Destroy = Resources.get(strings.Destroy);
	}

	public static class CLASS<T extends RoleTableAccess> extends Table.CLASS<T> {
		public CLASS() {
			this(null);
		}

		public CLASS(IObject container) {
			super(container);
			setJavaClass(RoleTableAccess.class);
			setName(TableName);
			setDisplayName(Resources.get(strings.Title));
		}

		@Override
		public Object newObject(IObject container) {
			return new RoleTableAccess(container);
		}
	}

	public SecurityGroups.CLASS<SecurityGroups> roles = new SecurityGroups.CLASS<SecurityGroups>(this);
	public Tables.CLASS<Tables> tables = new Tables.CLASS<Tables>(this);

	public Link.CLASS<Link> role = new Link.CLASS<Link>(this);
	public Link.CLASS<Link> table = new Link.CLASS<Link>(this);

	public BoolField.CLASS<BoolField> read = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> write = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> create = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> copy = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> destroy = new BoolField.CLASS<BoolField>(this);

	public RoleTableAccess() {
		this(null);
	}

	public RoleTableAccess(IObject container) {
		super(container);
	}

	@Override
	public void constructor1() {
		role.get(CLASS.Constructor1).operatorAssign(roles);
		table.get(CLASS.Constructor1).operatorAssign(tables);
	}

	@Override
	public void constructor2() {
		super.constructor2();

		roles.setIndex("roles");
		tables.setIndex("tables");

		role.setName(names.Role);
		role.setIndex("role");

		table.setName(names.Table);
		table.setIndex("table");

		read.setName(names.Read);
		read.setIndex("read");
		read.setDisplayName(displayNames.Read);
		read.get().setDefault(new bool(true));

		write.setName(names.Write);
		write.setIndex("write");
		write.setDisplayName(displayNames.Write);
		write.get().setDefault(new bool(true));

		create.setName(names.Create);
		create.setIndex("create");
		create.setDisplayName(displayNames.Create);
		create.get().setDefault(new bool(true));

		copy.setName(names.Copy);
		copy.setIndex("copy");
		copy.setDisplayName(displayNames.Copy);
		copy.get().setDefault(new bool(false));

		destroy.setName(names.Destroy);
		destroy.setIndex("destroy");
		destroy.setDisplayName(displayNames.Destroy);
		destroy.get().setDefault(new bool(false));

		registerDataField(role);
		registerDataField(table);

		registerDataField(read);
		registerDataField(write);
		registerDataField(create);
		registerDataField(copy);
		registerDataField(destroy);

		queries.add(roles);
		queries.add(tables);
	}
}