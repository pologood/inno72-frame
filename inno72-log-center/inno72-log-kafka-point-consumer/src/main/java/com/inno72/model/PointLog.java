package com.inno72.model;

import lombok.Data;

/**
 * 机器日志收集服务
 */
@Data
public class PointLog {

	/**
	 * kafka消费切点字段，如果POINT_TYPE有值则切到 PointLog.java 存储
	 */
	public static final String POINT_TYPE = "98u1fdvfuer2fw9iuebf1029e1";

	public static final String POINT_TYPE_WARNING = "1";
	public static final String POINT_TYPE_LOGIN = "31";
	public static final String POINT_TYPE_ORDER = "32";
	public static final String POINT_TYPE_COUPON_ORDER = "35";
	public static final String POINT_TYPE_FINISH = "33";
	public static final String POINT_TYPE_FANS = "34";

	/**
	 */
	private String machineCode;
	/**
	 * 日志类型
	 */
	private String type;
	/**
	 */
	private String pointTime;
	/**
	 */
	private String tag;
	/**
	 */
	private String detail;



}
