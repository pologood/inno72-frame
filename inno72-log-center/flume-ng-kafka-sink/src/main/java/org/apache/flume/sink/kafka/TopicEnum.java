package org.apache.flume.sink.kafka;



/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
