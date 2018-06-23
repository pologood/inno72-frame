package com.inno72.msg.center;

/**
 * 消息类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum MessageType {

	/**
	 * 微信
	 */
	WECHAT(1),
	/**
	 * 钉钉
	 */
	DINGDING(2),

	/**
	 * 短信
	 */
	SMS(3),
	/**
	 * 推送
	 */
	PUSH(4),
	/**
	 * 邮件
	 */
	MAIL(5),
	/**
	 * 钉钉机器人
	 */
	ROBOT(6),

	/**
	 * 钉钉微应用
	 */
	MINIAPP(7),

	/**
	 * 百度熊掌号
	 */
	XiongZhang(8),;

	private int v;

	private MessageType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
