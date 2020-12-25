package org.zenframework.z8.server.runtime;

import org.zenframework.z8.server.base.table.system.*;
import org.zenframework.z8.server.db.generator.DBGenerator;
import org.zenframework.z8.server.routing.AliasTable;
import org.zenframework.z8.server.routing.DBListener;

public class ServerRuntime extends AbstractRuntime {
	public ServerRuntime() {
		addTable(new Users.CLASS<Users>(null));
		addTable(new Roles.CLASS<Roles>(null));
		addTable(new Sequences.CLASS<Sequences>(null));
		addTable(new Settings.CLASS<Settings>(null));

		addTable(new Tables.CLASS<Tables>(null));
		addTable(new Fields.CLASS<Fields>(null));
		addTable(new Requests.CLASS<Requests>(null));

		addTable(new RoleTableAccess.CLASS<RoleTableAccess>(null));
		addTable(new RoleFieldAccess.CLASS<RoleFieldAccess>(null));
		addTable(new RoleRequestAccess.CLASS<RoleRequestAccess>(null));

		addTable(new UserRoles.CLASS<UserRoles>(null));

		addTable(new Domains.CLASS<Domains>(null));

		addTable(new Entries.CLASS<Entries>(null));
		addTable(new UserEntries.CLASS<UserEntries>(null));

		addTable(new Jobs.CLASS<Jobs>(null));
		addTable(new ScheduledJobs.CLASS<ScheduledJobs>(null));
		addTable(new ScheduledJobLogs.CLASS<ScheduledJobLogs>(null));

		addTable(new Files.CLASS<Files>(null));

		addTable(new MessageQueue.CLASS<MessageQueue>(null));
		addTable(new TransportQueue.CLASS<TransportQueue>(null));

		addEntry(new SystemTools.CLASS<SystemTools>(null));
		addRequest(new AliasTable.CLASS<>(null));
		DBGenerator.addListener(new DBListener());
	}
}
