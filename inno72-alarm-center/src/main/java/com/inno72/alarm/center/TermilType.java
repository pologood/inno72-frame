package com.inno72.alarm.center;

/**
 * 终端类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum TermilType {

	/**
	 * andriod
	 */
	ANDRIOD(1),
	/**
	 * ios
	 */
	IOS(2),
	/**
	 * 微信
	 */
	WECHAT(3),
	/**
	 * PC
	 */
	PC(4);
	

	private int v;

	private TermilType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
