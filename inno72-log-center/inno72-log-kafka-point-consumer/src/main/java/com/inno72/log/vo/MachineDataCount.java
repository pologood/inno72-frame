package com.inno72.log.vo;

import lombok.Data;

@Data
public class MachineDataCount {

	/**
	 * @MongoDB\Id(strategy="UUID")
	 */
	private String $id;
	/**
	 * 	String
	 * 	否	活动ID
	 */
	private String activityId;
	/**
	 * String
	 * 	否	日期	yyyy-MM-dd
	 */
	private String date;
	/**
	 * String
	 * 	否	机器Code
	 */
	private String machineCode;
	/**
	 *int
	 * 	否	总访客人数
	 */
	private Integer pv;
	/**
	 *int
	 * 	否	独立访客数
	 */
	private Integer uv;
	/**
	 *int
	 * 	否	订单量
	 */
	private Integer order;
	/**
	 *int
	 * 	否	出货量
	 */
	private Integer shipment;

	/**
	 * 新增粉丝量
	 */
	private Integer fans;

	public MachineDataCount() {
	}

	public MachineDataCount(String activityId, String date, String machineCode) {
		this.activityId = activityId;
		this.date = date;
		this.machineCode = machineCode;
		this.pv = 0;
		this.uv = 0;
		this.order = 0;
		this.shipment = 0;
		this.fans = 0;
	}

	public MachineDataCount order(){
		this.order += 1;
		return this;
	}
	public MachineDataCount pv(){
		this.pv += 1;
		return this;
	}
}
