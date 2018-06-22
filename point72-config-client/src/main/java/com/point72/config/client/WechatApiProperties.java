package com.point72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信接口地址
 * 
 * @author Houkm
 *
 *         2017年5月18日
 */
@ConfigurationProperties("wechat.api")
public class WechatApiProperties {

	private Msg msg;

	/**
	 * 微信消息接口
	 * 
	 * @author Houkm
	 *
	 *         2017年5月18日
	 */
	public static class Msg {
		/**
		 * 微信模板消息接口
		 */
		private String template;

		/**
		 * 微信普通消息接口(文本/图片/音频/视频等)
		 */
		private String normal;

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public String getNormal() {
			return normal;
		}

		public void setNormal(String normal) {
			this.normal = normal;
		}

	}

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

}
