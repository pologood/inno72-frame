package com.inno72.msg.center;

/**
 * 消息类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum MessageChildType {

	/**
	 * 文本
	 */
	TEXT(1),
	/**
	 * 微信模板
	 */
	TEMPLATE(2),
	/**
	 * 钉钉链接
	 */
	LINK(2),
	/**
	 * 云片短信
	 */
	YUNPIAN(1),
	/**
	 * 筑望短信
	 */
	ZHUWANG(2),
	/**
	 * 联江短信
	 */
	LIANJIANG(2),
	/**
	 * 自动切换通道短信
	 */
	AUTO(9);

	private int v;

	private MessageChildType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
