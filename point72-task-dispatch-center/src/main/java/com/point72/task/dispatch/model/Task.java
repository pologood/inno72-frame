package com.point72.task.dispatch.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.point72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

@Data
public class Task implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7576520820103205945L;

	private String id;
	private String taskModelId;
	private TaskModel taskModel;
	private String logName;

	/**
	 * 任务进入任务池时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registeTime;

	/**
	 * 解析cron后应该执行的时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime analyzeTime;

	/**
	 * 任务执行时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	/**
	 * 任务结束时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	/**
	 * 是否产生异常
	 */
	private boolean exception;

	/**
	 * 
	 */
	private String exceptionMessage;

}
