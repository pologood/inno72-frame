package com.inno72.log.center.vo;

public class BizInfo {

	/**
	 * dimension : sys, biz, product
	 * type : error
	 * tag : redis connection refused
	 * platform : java/go/php
	 * appName : info/warn/error
	 * instanceName : appName
	 * detail : detail
	 * time : time
	 * userId : userId
	 * operatorId : operatorId
	 * activityId : activityId
	 */
	private String dimension;
	private String type;
	private String tag;
	private String platform;
	private String appName;
	private String instanceName;
	private String detail;
	private String time;
	private String userId;
	private String operatorId;
	private String activityId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	@Override
	public String toString() {
		return "BizInfo{" + "dimension='" + dimension + '\'' + ", type='" + type + '\'' + ", tag='" + tag + '\''
				+ ", platform='" + platform + '\'' + ", appName='" + appName + '\'' + ", instanceName='" + instanceName
				+ '\'' + ", detail='" + detail + '\'' + ", time='" + time + '\'' + ", userId='" + userId + '\''
				+ ", operatorId='" + operatorId + '\'' + ", activityId='" + activityId + '\'' + '}';
	}
}
