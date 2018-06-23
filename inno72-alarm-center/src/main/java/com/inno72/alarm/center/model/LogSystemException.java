package com.inno72.alarm.center.model;

import lombok.Data;

@Data

public class LogSystemException extends LogException {

	private String msgId;

	public LogSystemException() {
	}

	public LogSystemException(LogException logEx) {
		this.caughtTime = logEx.caughtTime;
		this.id = logEx.id;
		this.notifyGroup = logEx.notifyGroup;
		this.notifyUser = logEx.notifyUser;
		this.owner = logEx.owner;
		this.project = logEx.project;
		this.stackTrace = logEx.stackTrace;
		this.title = logEx.title;
		this.applicationName = logEx.applicationName;
		this.message = logEx.message;
	}

}
