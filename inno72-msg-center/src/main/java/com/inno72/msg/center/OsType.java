package com.inno72.msg.center;

/**
 * 系统类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum OsType {

	/**
	 * 安卓版
	 */
	ANDRIOD(1),
	/**
	 * 苹果基础版
	 */
	IOS(2),
	/**
	 * 苹果专业版
	 */
	PRO(3);

	private int v;

	private OsType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
