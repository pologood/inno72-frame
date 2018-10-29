package com.inno72.util;

public enum TopicEnum {

	SYS("sys", "log-sys" ,"系统日志"),
	PRODUCT("product", "log-other" ,"产品日志"),
	BIZ("biz", "log-other" ,"业务日志"),
	POINT("point", "log-point" ,"埋点日志"),
	;

	private String logType;

	private String topic;

	private String info;

	TopicEnum(String logType, String topic, String info) {
		this.logType = logType;
		this.topic = topic;
		this.info = info;
	}

	public static TopicEnum findByType(String logType){
		TopicEnum[] values = TopicEnum.values();
		for ( TopicEnum topicEnum : values ){
			if (topicEnum.logType.equals(logType)){
				return topicEnum;
			}
		}
		return null;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
