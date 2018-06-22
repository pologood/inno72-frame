package com.point72.alarm.center.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.point72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * 问题
 * 
 * @author Houkm
 *
 *         2017年6月23日
 */
@Data
public class Question {
	private String id;
	private String num; // 编号
	private String title;
	private String content;
	private int termilType; // 1.android,2.ios,3.微信,4.PC
	private List<String> personLiable; // 责任人ID
	private List<String> personLiableName; // 责任人
	private String questionSource; // 问题来源
	private String createBy;
	private String createByName;
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime;
	private String modifyBy;
	private String modifByname;
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime modifyTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

}
