package com.point72.task.dispatch.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskExecutorModel {

	public enum ExecutorStatus {
		RUNNING, STOP;
	}

	private String id;

	private String filePath;

	private ExecutorStatus status;

	private LocalDateTime createTime;

}
