package com.point72.msg.center.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.point72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * 消息对象
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
@Data
public class MsgModel {

	/**
	 * 模板对象
	 */
	private MsgTemplateModel model;
	private String code; // 模板code

	private int messageType; // 消息类型

	private String title; // 邮件标题，其他没有此字段

	/**
	 * 具体消息内容
	 */
	private AbstractMsgModel content; // 文本：显示文本的内容，模板：消息对象

	private String sentBy; // 发送人

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime sentTime; // 发送时间
	private String status; // 1成功0失败
	private String statusMessage;// 状态消息
	private String receiver; // 接受人,钉钉企业会话时为微应用的ID

	private String result;// 存各平台请求返回数据

	private String id; // 唯一标示

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

	/**
	 * 是否通过机器人发送
	 */
	@Transient
	private boolean robot;

	/**
	 * 是否通过微应用发送
	 */
	@Transient
	private boolean miniApp;

	/**
	 * 钉钉微应用接收人
	 */
	@Transient
	private List<String> userIds;

	/**
	 * 接收消息的微应用ID
	 */
	@Transient
	private String agentId;

}
