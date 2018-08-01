package com.inno72.core.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class MsgDTO implements Serializable {

	public final static String MQ_KEY_PREFIXX = "mq_msg_";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2334687262915572024L;

	private String code; // 模板code
	private String title; // 邮件消息标题
	/**
	 * 填充模板文件中的参数
	 */
	private Map<String, String> params;
	private String sentBy; // 发送人
	private String receiver; // 接受人：手机号或者openId或者设备号
	/**
	 * 所需其他参数
	 */
	private Map<String, String> addedParams;

	private final String msgTimestamp = MQ_KEY_PREFIXX + System.currentTimeMillis();

}
