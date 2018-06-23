package com.inno72.msg.center;

/**
 * 透传消息启动类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum TransmissionType {

	/**
	 * 立即启动
	 */
	RESTART_NOW(1),
	/**
	 * 广播等待客户端自启动
	 */
	BRODCAST_WAIT_CLIENT_START(2);

	private int v;

	private TransmissionType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
