package com.inno72.log.vo;


import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "MachineGoodsCount")
public class MachineGoodsCount implements Serializable {

	private Object id;

	/**
	 * 	String
	 * 	否	时间	yyyy-MM-dd
	 */
	private String time;
	/**
	 * String
	 * 	否	活动Id
	 */
	private String activityId;
	/**
	 *String
	 * 	否	机器Code
	 */
	private String machineCode;
	/**
	 *String
	 * 	否	商品Code
	 */
	private String goodsCode;
	/**
	 *String
	 * 	否	商品名称
	 */
	private String goodsName;
	/**
	 *int
	 * 	否	商品数量
	 */
	private Integer goods;

	public MachineGoodsCount() {
	}

	public MachineGoodsCount(String time, String activityId, String machineCode, String goodsCode) {
		this.time = time;
		this.activityId = activityId;
		this.machineCode = machineCode;
		this.goodsCode = goodsCode;
		this.goods = 0;
	}
	public MachineGoodsCount goods(){
		this.goods+=1;
		return this;
	}
}
