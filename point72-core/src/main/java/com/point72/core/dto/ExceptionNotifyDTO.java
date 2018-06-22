package com.point72.core.dto;

import java.io.Serializable;

public class ExceptionNotifyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5234256163799954290L;

	private RuntimeException ex;
	private String[] userId;
	private String groupId;
	private boolean notifyDev;
	private String title;
	private String owner;
	private String termilType;

	public RuntimeException getEx() {
		return ex;
	}

	public void setEx(RuntimeException ex) {
		this.ex = ex;
	}

	public String[] getUserId() {
		return userId;
	}

	public void setUserId(String[] userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isNotifyDev() {
		return notifyDev;
	}

	public void setNotifyDev(boolean notifyDev) {
		this.notifyDev = notifyDev;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTermilType() {
		return termilType;
	}

	public void setTermilType(String termilType) {
		this.termilType = termilType;
	}

}
