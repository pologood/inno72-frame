package com.inno72.alarm.center.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.annotation.ExceptionNotify;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import com.inno72.common.utils.StringUtil;
import com.inno72.core.dto.LogExceptionDTO;

import lombok.Data;

@Data
public class LogException {

	public LogException(LogExceptionDTO dto) {
		ExceptionNotify en = dto.getExceptionNotify();
		Exception exception = dto.getEx();
		// 注解信息
		if (en != null) {
			this.setNotifyGroup(en.notifyGroup());
			this.setNotifyUser(en.notifyUser());
			this.setOwner(en.owner());
			this.setTitle(en.title());
			this.setProject(en.project());
			this.setFunction(en.function());
		}
		this.setCaughtTime(dto.getCaughtTime());
		this.setId(StringUtil.uuid());
		this.setStackTrace(com.inno72.alarm.center.model.StackTraceElement.transfer(exception.getStackTrace()));
		this.exceptionClass = exception.getClass().getName();
		this.setMessage(
				//@formatter:off
				exception.getMessage() == null 
				? 
						exception.getLocalizedMessage() == null
				? 
						exception.getClass().getSimpleName() 
						: exception.getLocalizedMessage() 
						: exception.getMessage()
				//@formatter:on
		);
		this.applicationName = dto.getApplicationName();
	}

	public LogException() {
	}

	@Override
	public String toString() {
		//@formatter:off
		return this.project==null||"".equals(this.project)
				?
						(
								this.applicationName==null||"".equals(this.applicationName)
								?
										"unknow"
										:this.applicationName)
										:this.project;
		//@formatter:on
	}

	protected String id;

	/**
	 * 异常责任人
	 */
	protected String owner;

	/**
	 * 异常标题
	 */
	protected String title;

	/**
	 * 报警通知人
	 */
	protected String[] notifyUser;

	/**
	 * 报警通知群
	 */
	protected String notifyGroup;

	/**
	 * 产生异常的项目工程
	 */
	protected String project;

	/**
	 * 产生异常的功能
	 */
	protected String function;

	/**
	 * 异常信息
	 */
	protected String message;

	/**
	 * 异常堆栈
	 */
	protected List<StackTraceElement> stackTrace;

	/**
	 * 取自产生异常的项目的spring.application.name配置
	 */
	protected String applicationName;

	/**
	 * 异常类
	 */
	protected String exceptionClass;

	/**
	 * 异常产生时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime caughtTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime startTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime endTime;

	@Transient
	protected String keyword;

}
