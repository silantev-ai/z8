package org.zenframework.z8.server.base.table.system;

import org.zenframework.z8.server.base.table.Table;
import org.zenframework.z8.server.base.table.value.BoolField;
import org.zenframework.z8.server.base.table.value.Link;
import org.zenframework.z8.server.resources.Resources;
import org.zenframework.z8.server.runtime.IClass;
import org.zenframework.z8.server.runtime.IObject;

public class UserTableAccess extends Table {
	final static public String TableName = "SystemUserTableAccess";

	static public class fieldNames {
		public final static String User = "User";
		public final static String Table = "Table";
		public final static String Read = "Read";
		public final static String Write = "Write";
		public final static String Create = "Create";
		public final static String Copy = "Copy";
		public final static String Destroy = "Destroy";
	}

	static public class strings {
		public final static String Title = "UserTableAccess.title";
		public final static String Read = "UserTableAccess.read";
		public final static String Write = "UserTableAccess.write";
		public final static String Create = "UserTableAccess.create";
		public final static String Copy = "UserTableAccess.copy";
		public final static String Destroy = "UserTableAccess.destroy";
	}

	static public class displayNames {
		public final static String Title = Resources.get(strings.Title);
		public final static String Read = Resources.get(strings.Read);
		public final static String Write = Resources.get(strings.Write);
		public final static String Create = Resources.get(strings.Create);
		public final static String Copy = Resources.get(strings.Copy);
		public final static String Destroy = Resources.get(strings.Destroy);
	}

	public static class CLASS<T extends UserTableAccess> extends Table.CLASS<T> {
		public CLASS() {
			this(null);
		}

		public CLASS(IObject container) {
			super(container);
			setJavaClass(UserTableAccess.class);
			setName(TableName);
			setDisplayName(displayNames.Title);
		}

		@Override
		public Object newObject(IObject container) {
			return new UserTableAccess(container);
		}
	}

	public Users.CLASS<Users> users = new Users.CLASS<Users>(this);
	public Tables.CLASS<Tables> tables = new Tables.CLASS<Tables>(this);

	public Link.CLASS<Link> user = new Link.CLASS<Link>(this);
	public Link.CLASS<Link> table = new Link.CLASS<Link>(this);

	public BoolField.CLASS<BoolField> read = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> write = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> create = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> copy = new BoolField.CLASS<BoolField>(this);
	public BoolField.CLASS<BoolField> destroy = new BoolField.CLASS<BoolField>(this);

	public UserTableAccess() {
		this(null);
	}

	public UserTableAccess(IObject container) {
		super(container);
	}

	@Override
	public void constructor1() {
		user.get(IClass.Constructor1).operatorAssign(users);
		table.get(IClass.Constructor1).operatorAssign(tables);
	}

	@Override
	public void initMembers() {
		super.initMembers();

		objects.add(user);
		objects.add(table);

		objects.add(read);
		objects.add(write);
		objects.add(create);
		objects.add(copy);
		objects.add(destroy);

		objects.add(users);
		objects.add(tables);
	}
	@Override
	public void constructor2() {
		super.constructor2();

		users.setIndex("users");
		tables.setIndex("tables");

		user.setName(fieldNames.User);
		user.setIndex("user");

		table.setName(fieldNames.Table);
		table.setIndex("table");

		read.setName(fieldNames.Read);
		read.setIndex("read");
		read.setDisplayName(displayNames.Read);

		write.setName(fieldNames.Write);
		write.setIndex("write");
		write.setDisplayName(displayNames.Write);

		create.setName(fieldNames.Create);
		create.setIndex("create");
		create.setDisplayName(displayNames.Create);

		copy.setName(fieldNames.Copy);
		copy.setIndex("copy");
		copy.setDisplayName(displayNames.Copy);

		destroy.setName(fieldNames.Destroy);
		destroy.setIndex("destroy");
		destroy.setDisplayName(displayNames.Destroy);
	}
}