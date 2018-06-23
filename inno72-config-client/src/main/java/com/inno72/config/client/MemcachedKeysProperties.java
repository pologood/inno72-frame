package com.inno72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Memcached常用key配置
 * 
 * @author Houkm
 *
 *         2017年5月18日
 */
@ConfigurationProperties(prefix = "yyxk.memcached.keys")
public class MemcachedKeysProperties {

	/**
	 * 公众号Token Key
	 */
	private String wxtoken;
	/**
	 * 公众号Ticket Key
	 */
	private String wxticket;
	/**
	 * 企业号Token Key
	 */
	private String qytoken;
	/**
	 * 企业号Ticket Key
	 */
	private String qyticket;
	/**
	 * 钉钉Token Key
	 */
	private String ddtoken;
	/**
	 * 钉钉Ticket Key
	 */
	private String ddticket;

	public String getWxtoken() {
		return wxtoken;
	}

	public void setWxtoken(String wxtoken) {
		this.wxtoken = wxtoken;
	}

	public String getWxticket() {
		return wxticket;
	}

	public void setWxticket(String wxticket) {
		this.wxticket = wxticket;
	}

	public String getQytoken() {
		return qytoken;
	}

	public void setQytoken(String qytoken) {
		this.qytoken = qytoken;
	}

	public String getQyticket() {
		return qyticket;
	}

	public void setQyticket(String qyticket) {
		this.qyticket = qyticket;
	}

	public String getDdtoken() {
		return ddtoken;
	}

	public void setDdtoken(String ddtoken) {
		this.ddtoken = ddtoken;
	}

	public String getDdticket() {
		return ddticket;
	}

	public void setDdticket(String ddticket) {
		this.ddticket = ddticket;
	}

}
