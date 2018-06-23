package com.inno72.alarm.center.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * 问题跟踪列表(报错)
 * 
 * @author Houkm
 *
 *         2017年6月23日
 */
@Data
public class Follow {
	private String id;
	private String questionId; // 问题ID
	private String followContent; // 报错描述
	private String solutionContent; // 解决描述
	private String errorContent; // 错误原因
	private int state; // 1报错状态 2解决状态

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime; // 报错时间
	private String createBy; // 报错人
	private String createByName;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime solutionTime; // 解决时间
	private String solutionBy; // 解决人
}
