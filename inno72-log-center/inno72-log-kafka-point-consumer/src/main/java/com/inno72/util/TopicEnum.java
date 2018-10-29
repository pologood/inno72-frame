package com.inno72.util;

public class TopicEnum {

	public static class SYS{//elasticSearch
		public static final String logType = "sys";
		public static final String topic = "log-sys";
		public static final String info = "系统日志";
	}
	public static class PRODUCT{//mongodb
		public static final String logType = "product";
		public static final String topic = "log-other";
		public static final String info = "产品日志";
	}
	public static class BIZ{//mongodb
		public static final String logType = "biz";
		public static final String topic = "log-other";
		public static final String info = "业务日志";
	}
	public static class POINT{//mongodb
		public static final String logType = "point";
		public static final String topic = "log-point";
		public static final String info = "机器埋点日志";
	}

}
