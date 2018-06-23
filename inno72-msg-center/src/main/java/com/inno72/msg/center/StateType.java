package com.inno72.msg.center;

public enum StateType {
	/**
	 * 正常
	 */
	NOMAL("1"),
	/**
	 * 删除
	 */
	DEL("0"),
	/**
	 * 成功
	 */
	SUCCESS("0"),
	/**
	 * 失败
	 */
	FAILURE("1");

	private String v;

	private StateType(String v) {
		this.v = v;
	}

	public String getV() {
		return v;
	}

}
