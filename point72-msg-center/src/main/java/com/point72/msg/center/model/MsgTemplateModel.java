package com.point72.msg.center.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.point72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * 消息模板对象
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
@Data
public class MsgTemplateModel {

	private AbstractMsgModel content; // 文本和链接时为字符串，其他为对象
	private String id; // 同Code
	private String name; // 模板名称
	private String code; // 模板Code
	private Integer messageType; // 消息类型
	private Integer messageChildType; // 消息子类型
	private String receiver; // 接收人（钉钉必填接收人）
	private String createBy; // 创建人

	// @DateTimeFormat(iso = ISO.DATE_TIME)
	// @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime; // 创建时间
	private String modifyBy; // 修改人
	// @DateTimeFormat(iso = ISO.DATE_TIME)
	// @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime modifyTime; // 修改时间
	private String state; // 状态
	private SendTime sendTime; // 发送时间段

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime startTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime endTime;

	@Transient
	private String key;

}