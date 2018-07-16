package com.inno72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yyxk.mq.queue")
public class MqProperties {

	private Queue msg;
	private Queue custom;
	private Queue exceptionNotify;
	private Queue logException;

	public static class Queue {
		private String name;
		private String exchange;
		private String key;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getExchange() {
			return exchange;
		}

		public void setExchange(String exchange) {
			this.exchange = exchange;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	public Queue getMsg() {
		return msg;
	}

	public void setMsg(Queue msg) {
		this.msg = msg;
	}

	public Queue getExceptionNotify() {
		return exceptionNotify;
	}

	public void setExceptionNotify(Queue exceptionNotify) {
		this.exceptionNotify = exceptionNotify;
	}

	public Queue getLogException() {
		return logException;
	}

	public void setLogException(Queue logException) {
		this.logException = logException;
	}

	public Queue getCustom() {
		return custom;
	}

	public void setCustom(Queue custom) {
		this.custom = custom;
	}

}
