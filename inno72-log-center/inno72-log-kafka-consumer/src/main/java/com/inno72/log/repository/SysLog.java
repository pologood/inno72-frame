package com.inno72.log.repository;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "inno72_dababase", type = "syslog")
public class SysLog {

	/**
	 *{"logType":"sys","platform":"java","appName":"后台游戏服务","instanceName":"172.16.23.216:null","level":"INFO","time":"2018-07-20 15:41:02","tag":"系统日志tag","detail":"com.company.project.web.UserController.list(UserController.java:60) hello sys log\n"}
	 */
	private String id;

	/**
	 *  类型	 系统日志sys 产品日志product 业务日志 biz
	 */
	private String logType;

	/**
	 *  日志级别	同log4j
	 */
	private String level;
	/**
	 *  标签（便于查询）	例如：调用聚石塔接口
	 */
	private String tag;

	/**
	 *  平台	java / h5 / android
	 */
	private String platform;

	/**
	 * 	应用名称	例如： 游戏后台服务
	 */
	private String appName;

	/**
	 *  实例名称（区分相同应用，不用实例）	例如： java 服务 (ip:端口), Android H5（机器id
	 */
	private String instanceName;

	/**
	 *  详情	任何想记录的内容
	 */
	private String detail;

	/**
	 *  日志时间	例如：2018-07-10 12:00:00
	 */
	@Field(format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
	private String time;

}
