package com.point72.msg.center;

/**
 * 透传消息模板类型
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
public enum TransmissionTemplateType {
	
	/**
	 * 点击通知打开应用模板
	 */
	NOTIFICATION_OPEN_APPLICATION(1),
	/**
	 * 点击通知打开网页模板(暂不支持)
	 */
	NOTIFICATION_OPEN_WEB(2),
	/**
	 * 点击通知弹窗下载模板（暂不支持）
	 */
	NOTIFICATION_POPUP(3),
	/**
	 * 透传消息模版
	 */
	TRANSMISSION_MSG(4);

	private int v;

	private TransmissionTemplateType(int v) {
		this.v = v;
	}

	public int v() {
		return v;
	}

}
