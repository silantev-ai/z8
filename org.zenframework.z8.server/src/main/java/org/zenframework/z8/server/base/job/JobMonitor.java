package org.zenframework.z8.server.base.job;

import java.util.Collections;

import org.zenframework.z8.server.engine.ApplicationServer;
import org.zenframework.z8.server.json.Json;
import org.zenframework.z8.server.json.JsonWriter;
import org.zenframework.z8.server.request.Monitor;
import org.zenframework.z8.server.types.date;
import org.zenframework.z8.server.types.exception;
import org.zenframework.z8.server.types.string;

public class JobMonitor extends Monitor {

	private final Job job;
	private int total = 100;
	private int worked = 0;
	private date start = new date();

	public JobMonitor(Job job, String id) {
		super(id);
		this.job = job;
	}

	@Override
	public String displayName() {
		return job != null ? job.displayName() : null;
	}

	public Job getJob() {
		return job;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getWorked() {
		return worked;
	}

	public void setWorked(int worked) {
		this.worked = worked;
	}

	@Override
	public void info(String text) {
		super.info(text);

		if(job != null && job.scheduled())
			logInfo(text);
	}

	@Override
	public void warning(String text) {
		super.warning(text);

		if(job != null && job.scheduled())
			logWarning(text);
	}

	@Override
	public void error(Throwable exception) {
		super.error(exception);

		if(job != null && (job.scheduled() || !(exception instanceof exception)))
			logError(exception);
	}

	public boolean isDone() {
		return job != null ? job.isDone() : true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeResponse(JsonWriter writer) {
		synchronized(this) {
			boolean isDone = isDone();

			writer.writeProperty(Json.isJob, true);
			writer.writeProperty(Json.id, id());

			writer.writeProperty(Json.done, isDone);
			writer.writeProperty(Json.total, total);
			writer.writeProperty(Json.worked, worked);
			writer.writeProperty(Json.start, start);
			writer.writeProperty(Json.duration, new date().operatorSub(start));

			writer.writeProperty(new string(Json.server), ApplicationServer.id);
			writer.writeInfo(getMessages(), isDone ? getFiles() : Collections.EMPTY_LIST, ApplicationServer.id);

			clearMessages();

			if(isDone)
				Job.removeMonitor(this);
		}
	}
}
