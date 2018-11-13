package com.inno72.msg.center.model;

import java.util.Map;

import lombok.Data;

@Data
public class PushModel extends AbstractMsgModel {

	private String content; // 透传内容，不支持转义字符
	private int transmissionType; // 收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动

	/**
	 * 1. 点击通知打开应用模板 2. 点击通知打开网页模板(暂不支持) 3. 点击通知弹窗下载模板（暂不支持） 4. 透传消息模版
	 */
	private int templateType;

	private int osType; // 系统类型 1为andriod 2为ios基础版 3为ios专业版
	
	private int appType;// 1 机器push 2 巡检push

	/**
	 * 通知消息的title
	 */
	private String title;
	/**
	 * 通知消息的text
	 */
	private String text;

	/**
	 * 标签
	 */
	private String tags;

	/**
	 * 通知铃声文件名
	 */
	private String sound = "default";

	private Map<String, String> tplParam;
}
