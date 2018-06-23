package com.inno72.alarm.center.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * 解决方案
 * 
 * @author Houkm
 *
 *         2017年6月23日
 */
@Data
public class Solution {
	private String id;
	private String questionId;
	private int num; //方案顺序
	private String content; //方案描述
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime;
	private String createBy;
	
}
